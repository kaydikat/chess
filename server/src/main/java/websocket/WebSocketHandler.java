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
import model.UserData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import result.JoinGameResult;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.SessionManager;
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
      }
      // case RESIGN -> resign(session, username, (ResignCommand) command);
  }

  private void connect(Session session, String username, String color, ConnectCommand command) {
    try {
    sessions.add(command.getGameID(), session);
    loadGame(command.getGameID(), session, command);
    notifyConnect(username, color, command);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void makeMove(Session session, String username, MakeMoveCommand command) {
    try {
      GameDao gameDao = GameDaoSQL.getInstance();
      GameData gameData = gameDao.getGame(command.getGameID());
      ChessGame game = gameData.game();
      game.makeMove(command.getMove());
      gameDao.updateGame(command.getGameID(), game);
      loadGame(command.getGameID(), session, command);
      notifyMakeMove(username, command.getMove(), command);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void leaveGame(Session session, String username, LeaveGameCommand command) {
    try {
      GameDao gameDao = GameDaoSQL.getInstance();
      gameDao.removeColor(command.getGameID(), getColor(command.getGameID(), username));
      String message = String.format("User " + username + " left game " + command.getGameID());
      ServerMessage serverMessage = new NotificationMessage(message);
      broadcast(command.getGameID(), serverMessage);
      sessions.remove(command.getGameID(), session);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private String getUsername(String authToken) throws DataAccessException {
    AuthDao authDao =AuthDaoSQL.getInstance();
    return authDao.getUsername(authToken);
  }

  private String getColor(Integer gameID, String username) throws DataAccessException {
    GameDao gameDao = GameDaoSQL.getInstance();
    return gameDao.getPlayerColor(gameID, username);
  }

  private void broadcast(Integer gameID, ServerMessage message) {
    try {
      sessions.broadcast(gameID, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadGame(Integer gameID, Session session, UserGameCommand command) {
    try {
      GameDao gameDao = GameDaoSQL.getInstance();
      GameData gameData = gameDao.getGame(gameID);
      ChessGame game = gameData.game();
      String playerColor = getColor(gameID, getUsername(command.getAuthString()));
      ServerMessage loadGameMessage = new LoadGameMessage(game, playerColor);
      broadcast(command.getGameID(), loadGameMessage);
    } catch (Exception e) {
      error(session, e.getMessage());
    }
  }

  private void notifyConnect(String username, String color, ConnectCommand command) {
    if (color.equalsIgnoreCase("observer")) {
      color = "observer";
    }
    String message = String.format("User " + username +
            " connected to game " + command.getGameID() +
            " as " + color);
    ServerMessage serverMessage = new NotificationMessage(message);
    broadcast(command.getGameID(), serverMessage);
  }

  private void notifyMakeMove(String username, ChessMove move, MakeMoveCommand command) {
    String message = String.format("User " + username +
            " made move " + move);
    ServerMessage serverMessage = new NotificationMessage(message);
    broadcast(command.getGameID(), serverMessage);
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