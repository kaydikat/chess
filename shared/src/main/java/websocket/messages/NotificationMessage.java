package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String message;
    public NotificationMessage(String message) {
        super(ServerMessageType.NOTIFICATION, message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
