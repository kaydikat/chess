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
  private static final String X = " X ";
  private static final String O = " O ";
  private static Random rand = new Random();


  public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    drawHeaders(out);

    drawChessBoard(out);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static void drawHeaders(PrintStream out) {

    setBlack(out);

    String[] letterHeaders = { "A", "B", "C", "D", "E", "F", "G", "H" };
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawHeader(out, letterHeaders[boardCol]);
    }

    out.println();
  }

  private static void drawHeader(PrintStream out, String headerText) {
    int prefixLength = 1;
    int suffixLength = 0;

    out.print(EMPTY.repeat(prefixLength));
    printHeaderText(out, headerText);
  }

  private static void printHeaderText(PrintStream out, String player) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_GREEN);

    out.print(player);

    setBlack(out);
  }

  private static void drawChessBoard(PrintStream out) {

    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES / 2; ++boardRow) {
        drawRowOfSquares(out, "black");
        drawRowOfSquares(out, "white");
    }
  }

  private static void drawRowOfSquares(PrintStream out, String playerColor) {

      for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
        setRed(out);

          if (boardCol % 2 == 0) {
            if (playerColor.equals("black")) {
              drawBlackSquare(out);
            } else {
              drawWhiteSquare(out);
            }
          } else {
            if (playerColor.equals("black")) {
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
