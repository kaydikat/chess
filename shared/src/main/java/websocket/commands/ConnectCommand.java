package websocket.commands;

import model.UserData;

public class ConnectCommand extends UserGameCommand {
  private final String username;

  public ConnectCommand(String authToken, Integer gameID, String username) {
    super(CommandType.CONNECT, authToken, gameID);
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
