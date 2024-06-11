package websocket;

import javax.websocket.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.websocket.Session;

@WebSocket
public class WebSocketHandler {

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws Exception {
    //session.getRemote().sendString("WebSocket response: " + message);
  }

}
