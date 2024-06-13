package client;

import java.util.Scanner;
import websocket.ServerMessageObserver;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver {
  private final ChessClient client;

  public Repl(String serverUrl) throws Exception {
    client = new ChessClient(serverUrl, this);
  }

  public void run() {
    System.out.println(SET_TEXT_COLOR_WHITE + "Welcome to 240 chess.");
    System.out.print(client.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }


  @Override
  public void notify(ServerMessage message) {
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
//      case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
//      case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
    }
  }

    private void displayNotification(String message) {
        System.out.println(SET_TEXT_COLOR_BLUE + message);
    }


  private void printPrompt() {
    System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }

}
