package ui;
//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl { //implements NotificationHandler {
  private final ChessClient client;

  public Repl(String serverUrl) {
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


  private void printPrompt() {
    System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }

}
