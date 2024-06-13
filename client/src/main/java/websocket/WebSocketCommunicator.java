package websocket;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.google.gson.Gson;
import responseexception.ResponseException;
import websocket.messages.ServerMessage;
import websocket.ServerMessageObserver;

public class WebSocketCommunicator extends Endpoint {
  private Session session;
  private final ServerMessageObserver observer;
  private final Gson gson = new Gson();

  public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws Exception {
    this.observer = observer;
    serverUrl = serverUrl.replace("http", "ws");
    URI uri = new URI(serverUrl + "/ws");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);

    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      @Override
      public void onMessage(String message) {
        try {
          ServerMessage serverMessage =
                  gson.fromJson(message, ServerMessage.class);
          observer.notify(serverMessage);
        } catch(Exception ex) {
          observer.notify(new ErrorMessage(ex.getMessage()));
        }
      }
    });
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    this.session = session;
    System.out.println("Connected to WebSocket server");
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    System.out.println("WebSocket connection closed: " + closeReason.getReasonPhrase());
  }

  public void send(String msg) throws Exception {
    this.session.getBasicRemote().sendText(msg);
  }
}

