package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private String loadMessage;
    private ChessGame game;
    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String getGame() {
        return loadMessage;
    }
}
