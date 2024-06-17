package client;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ui.ChessBoardUi;
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
      case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame(),
              ((LoadGameMessage) message).getPlayerColor());
    }
  }

    private void displayNotification(String message) {
        System.out.println(SET_TEXT_COLOR_BLUE + message);
    }
    private void displayError(String message) {
        System.out.println(SET_TEXT_COLOR_RED + message);
    }
  private void loadGame(ChessGame game, String playerColor) {
    ChessBoard board = game.getBoard();
    new ChessBoardUi(board);
    if (playerColor.equalsIgnoreCase("black")) {
      ChessBoardUi.drawBoard(System.out, "black");
    } else {
      ChessBoardUi.drawBoard(System.out, "white");
    }
  }


  private void printPrompt() {
    System.out.print("\n" + RESET_BG_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
  }

}
