package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoardUi {

  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;
  private static final int LINE_WIDTH_IN_CHARS = 1;
  private static final String EMPTY = "   ";
  private static Random rand = new Random();


  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    drawHeaders(out, "white");

    drawChessBoard(out, "white");

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawHeaders(PrintStream out, String player) {
    setBlack(out);
    String[] letterHeaders = { "A", "B", "C", "D", "E", "F", "G", "H" };
    out.print(" ".repeat(SQUARE_SIZE_IN_CHARS) );
    if (player.equalsIgnoreCase("black")) {
      for (int boardCol = 7; boardCol >= 0; --boardCol) {
        drawHeader(out, letterHeaders[boardCol]);
      }
    } else {
      for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
        drawHeader(out, letterHeaders[boardCol]);
      }
    }

    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {
    int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
    int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

    out.print(" ".repeat(prefixLength));
    printHeaderText(out, headerText);
    out.print(" ".repeat(suffixLength));
  }
  private static void printRowNumberHeader(PrintStream out, int boardRow) {
    printHeaderText(out, (" " + (8-boardRow) + " "));
  }


  private static void printHeaderText(PrintStream out, String player) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_GREEN);

    out.print(player);

    setBlack(out);
  }

  private static void drawChessBoard(PrintStream out, String playerColor) {
    if (playerColor.equalsIgnoreCase("black")) {
      for (int boardRow=7; boardRow > -1; --boardRow) {
        if (boardRow % 2 == 0) {
          drawRowOfSquares(out, "white", boardRow);
        } else {
          drawRowOfSquares(out, "black", boardRow);
        }
      }
    } else{
      for (int boardRow=0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
        if (boardRow % 2 == 0) {
          drawRowOfSquares(out, "white", boardRow);
        } else {
          drawRowOfSquares(out, "black", boardRow);
        }
      }
    }
    drawHeaders(out, playerColor);
  }
  private static void drawRowOfSquares(PrintStream out, String playerColor, int boardRow) {
    printRowNumberHeader(out, boardRow);
      for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
          if (boardCol % 2 == 0) {
            if (playerColor.equalsIgnoreCase("black")) {
              drawBlackSquare(out);
            } else {
              drawWhiteSquare(out);
            }
          } else {
            if (playerColor.equalsIgnoreCase("black")) {
              drawWhiteSquare(out);
            } else {
              drawBlackSquare(out);
            }
          }
      }
      setBlack(out);
      out.println();
  }

  private static void drawVerticalLine(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
    out.println(EMPTY);
  }

  private static void drawBlackSquare(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
  }

  private static void drawWhiteSquare(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
  }

  private static void setWhite(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void setRed(PrintStream out) {
    out.print(SET_BG_COLOR_RED);
    out.print(SET_TEXT_COLOR_RED);
  }

  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }

  private static void printPlayer(PrintStream out, String player) {
    out.print(SET_BG_COLOR_WHITE);
    out.print(SET_TEXT_COLOR_BLACK);

    out.print(player);

    setWhite(out);
  }
}
