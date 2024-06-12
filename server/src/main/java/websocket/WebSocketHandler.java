package websocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {
  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    System.out.println("Connected: " + session.getRemoteAddress().getAddress());
    session.getRemote().sendString("Welcome to WebSocket server");
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws Exception {
    System.out.println("Message received: " + message);
    session.getRemote().sendString("WebSocket response: " + message);
  }

  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    System.out.println("Connection closed: " + reason);
  }
}
