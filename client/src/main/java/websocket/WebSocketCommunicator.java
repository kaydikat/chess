package websocket;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class WebSocketCommunicator {
  private Session session;

  public WebSocketCommunicator(String serverUrl) throws Exception {
    serverUrl = serverUrl.replace("http", "ws");
    URI uri = new URI(serverUrl + "/ws");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    container.connectToServer(this, uri);
  }

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    System.out.println("Connected to WebSocket server");
  }

  @OnMessage
  public void onMessage(String message) {
    System.out.println("Message received from server: " + message);
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    System.out.println("WebSocket connection closed: " + closeReason.getReasonPhrase());
  }

  public void send(String msg) throws Exception {
    this.session.getBasicRemote().sendText(msg);
  }
}

