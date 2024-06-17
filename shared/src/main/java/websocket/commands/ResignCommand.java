package websocket.commands;

public class ResignCommand extends UserGameCommand {
    boolean resign = true;
    public ResignCommand(String authToken, Integer gameID, boolean resign) {
        super(CommandType.RESIGN, authToken, gameID);
        this.resign = resign;
    }

    public boolean getResign() {
        return resign;
    }
}
