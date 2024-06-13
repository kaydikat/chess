package websocket;

import websocket.messages.ServerMessage;

public class ErrorMessage extends ServerMessage {
  public ErrorMessage(String message) {
    super(ServerMessageType.ERROR, message);
  }
}
