package websocket;

import javax.websocket.*;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import responseexception.ResponseException;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;
import websocket.messages.ServerMessageDeserializer;

public class WebSocketCommunicator extends Endpoint {
  private Session session;
  private final ServerMessageObserver observer;
  private final Gson gson;

  public WebSocketCommunicator(String serverUrl, ServerMessageObserver observer) throws Exception {
    this.observer = observer;
    serverUrl = serverUrl.replace("http", "ws");
    URI uri = new URI(serverUrl + "/ws");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
    this.gson = builder.create();

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
  public void onOpen(Session session, EndpointConfig endpointConfig) {}

  public void send(String msg) throws Exception {
    this.session.getBasicRemote().sendText(msg);
  }
}

