package websocket.messages;

public class ErrorMessage extends ServerMessage {
  private String errorMessage;
  public ErrorMessage(String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
