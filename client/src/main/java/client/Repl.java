package client;

import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.ServerMessageObserver;
import websocket.messages.*;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageObserver {
  private Gson gson;
  private final ChessClient client;

  public Repl(String serverUrl) throws Exception {
    client = new ChessClient(serverUrl, this);
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
    this.gson = builder.create();
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
      case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
      case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
    }
  }

    private void displayNotification(String message) {
        System.out.println(SET_TEXT_COLOR_BLUE + message);
    }
    private void displayError(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
    }
    private void loadGame(String game) {
        System.out.println(SET_TEXT_COLOR_GREEN + game);
    }


  private void printPrompt() {
    System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }

}
