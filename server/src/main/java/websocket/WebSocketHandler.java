package websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.SessionManager;
import websocket.messages.NotificationMessage;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {
  private final SessionManager sessions = new SessionManager();
  protected final Gson gson = new Gson();

  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    System.out.println("Connected: " + session.getRemoteAddress().getAddress());
    ServerMessage message = new NotificationMessage("Welcome to WebSock server");
    session.getRemote().sendString(gson.toJson(message));
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String msg) throws Exception {
    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

    String username = getUsername(command.getAuthString());

    switch (command.getCommandType()) {
      case CONNECT -> connect(session, username, (ConnectCommand) command);
      // case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
      // case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
      // case RESIGN -> resign(session, username, (ResignCommand) command);
    }
  }

  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    System.out.println("Connection closed: " + reason);
  }

  private void connect(Session session, String username, ConnectCommand command) throws DataAccessException {
    sessions.add(command.getGameID(), session);
    String message = String.format("User " + username + " connected to game " + command.getGameID());
    ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    broadcast(command.getGameID(), serverMessage);
  }

  private String getUsername(String authToken) {
    return null; // Implement logic to get username from authToken
  }
  private void broadcast(Integer gameID, ServerMessage message) {
    try {
      sessions.broadcast(gameID, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}