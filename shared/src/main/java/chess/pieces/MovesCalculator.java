package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves=new ArrayList<ChessMove>();
    for (int[] direction : getDirections()) {
      calculateDirectionalMoves(board, myPosition, pieceColor, direction[0], direction[1], moves);
    }
    return moves;
  }

  protected abstract int[][] getDirections();

  private void calculateDirectionalMoves(ChessBoard board, ChessPosition startPosition, ChessGame.TeamColor pieceColor,
                                         int rowChange, int colChange, Collection<ChessMove> moves) {
    for (int i=1; ; i++) {
      int newRow=startPosition.getRow() + i * rowChange;
      int newCol=startPosition.getColumn() + i * colChange;
      if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
        break;
      }
      ChessPiece piece=board.getPiece(new ChessPosition(newRow, newCol));
      if (piece == null) {
        moves.add(new ChessMove(startPosition, new ChessPosition(newRow, newCol), null));
      } else if (piece.getTeamColor() != pieceColor) {
        moves.add(new ChessMove(startPosition, new ChessPosition(newRow, newCol), null));
        break; // Stop iteration if blocked by a piece of the opposite color
      } else {
        break; // Stop iteration if blocked by a piece of the same color
      }
    }
  }
}
