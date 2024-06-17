package websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import dataaccess.authdaos.AuthDao;
import dataaccess.authdaos.AuthDaoSQL;
import dataaccess.gamedaos.GameDao;
import dataaccess.gamedaos.GameDaoSQL;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.NotificationMessage;
import org.eclipse.jetty.websocket.api.Session;
@WebSocket
public class WebSocketHandler {
  private final SessionManager sessions = new SessionManager();
  protected final Gson gson;

  public WebSocketHandler() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(UserGameCommand.class, new CommandDeserializer());
    this.gson = builder.create();;
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

    Integer gameID = command.getGameID();
    String username = getUsername(command.getAuthString());
    String color = getColor(gameID, username);

    switch (command.getCommandType()) {
      case CONNECT -> connect(session, username, color, (ConnectCommand) command);
      case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
      case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
      case RESIGN -> resign(session, username, (ResignCommand) command);
    }
  }

  private void connect(Session session, String username, String color, ConnectCommand command) {
    try {
      sessions.add(command.getGameID(), session);
      loadGameConnect(command.getGameID(), session, command);
      notify(username, command, session);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void makeMove(Session session, String username, MakeMoveCommand command) {
    try {
      ChessGame game = getGame(command.getGameID());
      game.makeMove(command.getMove());
      GameDaoSQL.getInstance().updateGame(command.getGameID(), game);
      loadGameMakeMove(command.getGameID(), session, command);
      notify(username, command, session);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void leaveGame(Session session, String username, LeaveGameCommand command) {
    try {
      GameDao gameDao = GameDaoSQL.getInstance();
      gameDao.removeColor(command.getGameID(), getColor(command.getGameID(), username));
      notify(username, command, session);
      sessions.remove(command.getGameID(), session);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void resign(Session session, String username, ResignCommand command) {
    try {
      ChessGame game = getGame(command.getGameID());
      game.resign();
      notify(username, command, session);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private String getUsername(String authToken) throws DataAccessException {
    AuthDao authDao = AuthDaoSQL.getInstance();
    return authDao.getUsername(authToken);
  }

  private String getColor(Integer gameID, String username) throws DataAccessException {
    GameDao gameDao = GameDaoSQL.getInstance();
    return gameDao.getPlayerColor(gameID, username);
  }

  private void broadcast(Integer gameID, ServerMessage message, UserGameCommand command, Session session) {
    try {
      if (command.getCommandType() == UserGameCommand.CommandType.RESIGN) {
        sessions.broadcast(gameID, message);
      } else {
        sessions.broadcastToOthers(gameID, session, message);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadGameConnect(Integer gameID, Session session, UserGameCommand command) {
    try {
      ChessGame game = getGame(gameID);
      String playerColor = getColor(gameID, getUsername(command.getAuthString()));
      ServerMessage loadGameMessage = new LoadGameMessage(game, playerColor);
      sessions.broadcastToSelf(command.getGameID(), session, loadGameMessage);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }
  private void loadGameMakeMove(Integer gameID, Session session, UserGameCommand command) {
    try {
      ChessGame game = getGame(gameID);
      String playerColor = getColor(gameID, getUsername(command.getAuthString()));
      ServerMessage loadGameMessage = new LoadGameMessage(game, playerColor);
      sessions.broadcast(command.getGameID(), loadGameMessage);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }


  private ChessGame getGame(Integer gameID) throws DataAccessException {
    GameDao gameDao = GameDaoSQL.getInstance();
    GameData gameData = gameDao.getGame(gameID);
    return gameData.game();
  }

  private void notify(String username, UserGameCommand command, Session session) throws DataAccessException {
    String message;
    switch (command.getCommandType()) {
      case CONNECT -> {
        String color = getColor(command.getGameID(), username);
        message = String.format("User %s connected to game %d as %s", username, command.getGameID(), color);
      }
      case MAKE_MOVE -> {
        MakeMoveCommand makeMoveCommand = (MakeMoveCommand) command;
        message = String.format("User %s made move %s to %s", username, makeMoveCommand.getStart(), makeMoveCommand.getEnd());
      }
      case LEAVE -> message = String.format("User %s left game %d", username, command.getGameID());
      case RESIGN -> message = String.format("User %s resigned from game %d", username, command.getGameID());
      default -> throw new IllegalArgumentException("Unexpected command type: " + command.getCommandType());
    }
    ServerMessage serverMessage = new NotificationMessage(message);
    broadcast(command.getGameID(), serverMessage, command, session);
  }

  private void error(Session session, String message) {
    try {
      ServerMessage errorMessage = new ErrorMessage(message);
      session.getRemote().sendString(gson.toJson(errorMessage));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
