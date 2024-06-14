package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String notifyMessage;
    public NotificationMessage(String notifyMessage) {
        super(ServerMessageType.NOTIFICATION);
        this.notifyMessage = notifyMessage;
    }

    public String getMessage() {
        return notifyMessage;
    }
    @Override
    public String toString() {
        return "NotificationMessage{" +
                "message='" + getMessage() + '\'' +
                '}';
    }
}
