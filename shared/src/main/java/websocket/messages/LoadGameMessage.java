package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private String message;
    public LoadGameMessage(String message) {
        super(ServerMessageType.LOAD_GAME, message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
