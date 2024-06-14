package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.CommandDeserializer;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
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
    this.gson = builder.create();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
    gson.fromJson(msg, ConnectCommand.class);

    String username = getUsername(command.getAuthString());

    switch (command.getCommandType()) {
      case CONNECT -> connect(session, username, (ConnectCommand) command);
      // case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
      // case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
      // case RESIGN -> resign(session, username, (ResignCommand) command);
    }
  }

  private void connect(Session session, String username, ConnectCommand command) throws DataAccessException {
    sessions.add(command.getGameID(), session);
    String message = String.format("User " + username + " connected to game " + command.getGameID());
    ServerMessage serverMessage = new NotificationMessage(message);
    broadcast(command.getGameID(), serverMessage);
  }

  private String getUsername(String authToken) {
    return "username";
  }
  private void broadcast(Integer gameID, ServerMessage message) {
    try {
      sessions.broadcast(gameID, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}