package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();
    int[][] knightMoves = {
            {-2, -1}, {-2, 1}, // Up moves
            {-1, -2}, {-1, 2}, // Left moves
            {1, -2}, {1, 2},   // Right moves
            {2, -1}, {2, 1}    // Down moves
    };

    for (int[] move : knightMoves) {
      int newRow = myPosition.getRow() + move[0];
      int newColumn = myPosition.getColumn() + move[1];

      if (newRow >= 1 && newRow <= 8 && newColumn >= 1 && newColumn <= 8) {
        ChessPiece piece = board.getPiece(new ChessPosition(newRow, newColumn));
        if (piece == null || piece.getTeamColor() != pieceColor) {
          moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newColumn), null));
        }
      }
    }
    return moves;
  }
}
