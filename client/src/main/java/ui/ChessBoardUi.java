package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class ChessBoardUi {
  private static final ChessBoard chessBoard = new ChessBoard();
  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_CHARS = 3;

  public static void drawBoard(PrintStream out, String playerColor) {
    out.print(ERASE_SCREEN);

    drawHeaders(out, playerColor);
    drawChessBoard(out, playerColor);
    drawHeaders(out, playerColor);
  }


  private static void drawHeaders(PrintStream out, String player) {
    setBlack(out);
    String[] letterHeaders = {"A", "B", "C", "D", "E", "F", "G", "H"};
    out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
    if (player.equalsIgnoreCase("black")) {
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

  private static void drawChessBoard(PrintStream out, String playerColor) {
    chessBoard.resetBoard();
      for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
        drawTwoRows(out, boardRow, playerColor);
      }
  }

  private static void drawTwoRows(PrintStream out, int boardRow, String playerColor) {
    printRowNumberHeader(out, boardRow, playerColor);
    if (boardRow % 2 == 0) {
      drawRowOfSquares(out, "black", boardRow, playerColor);
    } else {
      drawRowOfSquares(out, "white", boardRow, playerColor);
    }
  }

  private static void drawRowOfSquares(PrintStream out, String playerColor, int boardRow, String viewingColor) {
    if (viewingColor.equalsIgnoreCase("black")) {
      for (int boardCol = BOARD_SIZE_IN_SQUARES; boardCol > 0; --boardCol) {
        drawSquareWithColor(out, playerColor, boardRow + 1, boardCol, viewingColor);
      }
    } else {
      for (int boardCol = 1; boardCol <= BOARD_SIZE_IN_SQUARES; ++boardCol) {
        drawSquareWithColor(out, playerColor, boardRow + 1, boardCol, viewingColor);
      }
    }
    printRowNumberHeader(out, boardRow, viewingColor);
    setBlack(out);
    out.println();
  }

  private static void drawSquareWithColor(PrintStream out, String playerColor, int boardRow, int boardCol, String viewingColor) {
    boolean isBlackSquare = (boardRow + boardCol) % 2 == 0;

    if (isBlackSquare) {
      drawBlackSquare(out, boardRow, boardCol, viewingColor);
    } else {
      drawWhiteSquare(out, boardRow, boardCol, viewingColor);
    }
  }

  private static void drawSquare(PrintStream out, int boardRow, int boardCol, boolean isBlack, String viewingColor) {
    ChessPiece piece = chessBoard.getPiece(new ChessPosition(boardRow, boardCol));
    if (isBlack) {
      out.print(SET_BG_COLOR_BLACK);
    } else {
      out.print(SET_BG_COLOR_WHITE);
    }

    if (piece != null) {
      switch (piece.getPieceType()) {
        case KING:
          printPlayer(out, " K ", piece.getTeamColor(), viewingColor);
          break;
        case QUEEN:
          printPlayer(out, " Q ", piece.getTeamColor(), viewingColor);
          break;
        case ROOK:
          printPlayer(out, " R ", piece.getTeamColor(), viewingColor);
          break;
        case BISHOP:
          printPlayer(out, " B ", piece.getTeamColor(), viewingColor);
          break;
        case KNIGHT:
          printPlayer(out, " N ", piece.getTeamColor(), viewingColor);
          break;
        case PAWN:
          printPlayer(out, " P ", piece.getTeamColor(), viewingColor);
          break;
        default:
          out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
          break;
      }
    } else {
      out.print(" ".repeat(SQUARE_SIZE_IN_CHARS));
    }
  }

  private static void drawBlackSquare(PrintStream out, int boardRow, int boardCol, String viewingColor) {
    drawSquare(out, boardRow, boardCol, true, viewingColor);
  }

  private static void drawWhiteSquare(PrintStream out, int boardRow, int boardCol, String viewingColor) {
    drawSquare(out, boardRow, boardCol, false, viewingColor);
  }

  private static void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_BLACK);
  }

  private static void printPlayer(PrintStream out, String player, ChessGame.TeamColor teamColor, String viewingColor) {
    out.print(SET_TEXT_BOLD);
    if (teamColor == ChessGame.TeamColor.WHITE) {
      if (viewingColor.equalsIgnoreCase("white")) {
        out.print(SET_TEXT_COLOR_RED);
      } else {
        out.print(SET_TEXT_COLOR_BLUE);
      }
    } else {
      if (viewingColor.equalsIgnoreCase("white")) {
        out.print(SET_TEXT_COLOR_BLUE);
      } else {
        out.print(SET_TEXT_COLOR_RED);
      }
    }
    out.print(player);
  }
}
