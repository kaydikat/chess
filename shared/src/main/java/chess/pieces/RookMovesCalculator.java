package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();

    // Iterate from the current row to row 1
    for (int i = myPosition.getRow() - 1; i >= 1; i--) {
      ChessPiece piece = board.getPiece(new ChessPosition(i, myPosition.getColumn()));
      if (piece == null) {
        moves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
      } else if (piece.getTeamColor() != pieceColor) {
        moves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
        break; // Stop iteration if blocked by a piece of the opposite color
      } else {
        break; // Stop iteration if blocked by a piece of the same color
      }
    }

    // Iterate from the current row to row 8
    for (int i = myPosition.getRow() + 1; i <= 8; i++) {
      ChessPiece piece = board.getPiece(new ChessPosition(i, myPosition.getColumn()));
      if (piece == null) {
        moves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
      } else if (piece.getTeamColor() != pieceColor) {
        moves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
        break; // Stop iteration if blocked by a piece of the opposite color
      } else {
        break; // Stop iteration if blocked by a piece of the same color
      }
    }

    // Iterate from the current column to column 1
    for (int i = myPosition.getColumn() - 1; i >= 1; i--) {
      ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), i));
      if (piece == null) {
        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
      } else if (piece.getTeamColor() != pieceColor) {
        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
        break; // Stop iteration if blocked by a piece of the opposite color
      } else {
        break; // Stop iteration if blocked by a piece of the same color
      }
    }

    // Iterate from the current column to column 8
    for (int i = myPosition.getColumn() + 1; i <= 8; i++) {
      ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), i));
      if (piece == null) {
        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
      } else if (piece.getTeamColor() != pieceColor) {
        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));
        break; // Stop iteration if blocked by a piece of the opposite color
      } else {
        break; // Stop iteration if blocked by a piece of the same color
      }
    }

    return moves;
  }
}
