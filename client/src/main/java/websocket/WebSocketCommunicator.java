package websocket;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
  private Session session;

  public WebSocketCommunicator(String serverUrl) throws Exception {
    serverUrl = serverUrl.replace("http", "ws");
    URI uri = new URI(serverUrl + "/connect");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);

    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      public void onMessage(String message) {
        System.out.println(message);
      }
    });
  }

  public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

