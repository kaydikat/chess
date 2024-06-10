package ui;

import java.io.PrintStream;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessGame;

import static ui.EscapeSequences.*;

public class ChessBoardUi {
  private static ChessBoard CHESS_BOARD;
  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;

  public ChessBoardUi(ChessBoard board) {
    CHESS_BOARD = board;
  }

  public static void drawBoard(PrintStream out, String playerColor) {
    out.print(ERASE_SCREEN);

    drawHeaders(out, playerColor);
    drawChessBoard(out, playerColor);
    drawHeaders(out, playerColor);
  }

  private static void drawHeaders(PrintStream out, String playerColor) {
    setBlack(out);
    String[] letterHeaders = {"A", "B", "C", "D", "E", "F", "G", "H"};
    out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
    if (playerColor.equalsIgnoreCase("black")) {
      for (int boardCol = 8; boardCol > 0; --boardCol) {
        drawHeader(out, letterHeaders[boardCol - 1]);
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

  private static void printRowNumberHeader(PrintStream out, int boardRow, String playerColor) {
    if (playerColor.equalsIgnoreCase("black")) {
      printHeaderText(out, " " + (boardRow + 1) + " ");
    } else {
      printHeaderText(out, " " + (8 - boardRow) + " ");
    }
  }

  private static void printHeaderText(PrintStream out, String text) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_GREEN);
    out.print(text);
    setBlack(out);
  }

  public static void resetBoard() {
    CHESS_BOARD.resetBoard();
  }

  private static void drawChessBoard(PrintStream out, String playerColor) {
    for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
      drawTwoRows(out, boardRow, playerColor);
    }
  }

  private static void drawTwoRows(PrintStream out, int boardRow, String playerColor) {
    printRowNumberHeader(out, boardRow, playerColor);
    for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
      drawSquareWithColor(out, boardRow, boardCol, playerColor);
    }
    printRowNumberHeader(out, boardRow, playerColor);
    setBlack(out);
    out.println();
  }

  private static void drawSquareWithColor(PrintStream out, int boardRow, int boardCol, String playerColor) {
    boolean isBlackSquare = (boardRow + boardCol) % 2 == 0;

    if (isBlackSquare) {
      drawBlackSquare(out, boardRow, boardCol, playerColor);
    } else {
      drawWhiteSquare(out, boardRow, boardCol, playerColor);
    }
  }

  private static void drawSquare(PrintStream out, int boardRow, int boardCol, boolean isBlack, String playerColor) {
    int actualRow = playerColor.equalsIgnoreCase("black") ? BOARD_SIZE_IN_SQUARES - boardRow - 1 : boardRow;
    int actualCol = playerColor.equalsIgnoreCase("black") ? BOARD_SIZE_IN_SQUARES - boardCol - 1 : boardCol;

    ChessPiece piece = CHESS_BOARD.getPiece(new ChessPosition(actualRow + 1, actualCol + 1)); // +1 to map to 1-based positions
    if (isBlack) {
      out.print(SET_BG_COLOR_WHITE);
    } else {
      out.print(SET_BG_COLOR_BLACK);
    }

    if (piece != null) {
      switch (piece.getPieceType()) {
        case KING:
          printPlayer(out, " K ", piece.getTeamColor());
          break;
        case QUEEN:
          printPlayer(out, " Q ", piece.getTeamColor());
          break;
        case ROOK:
          printPlayer(out, " R ", piece.getTeamColor());
          break;
        case BISHOP:
          printPlayer(out, " B ", piece.getTeamColor());
          break;
        case KNIGHT:
          printPlayer(out, " N ", piece.getTeamColor());
          break;
        case PAWN:
          printPlayer(out, " P ", piece.getTeamColor());
          break;
        default:
          out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
          break;
      }
    } else {
      out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
    }
  }

  private static void drawBlackSquare(PrintStream out, int boardRow, int boardCol, String playerColor) {
    drawSquare(out, boardRow, boardCol, true, playerColor);
  }

  private static void drawWhiteSquare(PrintStream out, int boardRow, int boardCol, String playerColor) {
    drawSquare(out, boardRow, boardCol, false, playerColor);
  }

  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }

  private static void printPlayer(PrintStream out, String player, ChessGame.TeamColor teamColor) {
    out.print(SET_TEXT_BOLD);
    if (teamColor == ChessGame.TeamColor.WHITE) {
      out.print(SET_TEXT_COLOR_RED);
    } else {
      out.print(SET_TEXT_COLOR_BLUE);
    }
    out.print(player);
  }
}
