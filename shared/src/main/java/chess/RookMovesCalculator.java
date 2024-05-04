package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();
    System.out.println("myPosition: " + myPosition.getRow() + " " + myPosition.getColumn());
    for (int i = 1; i <= 8; i++) {
      if (i != myPosition.getRow()) {
        moves.add(new ChessMove(myPosition, new ChessPosition(i, myPosition.getColumn()), null));
      }
    }
    for (int i = 1; i <= 8; i++) {
      if (i != myPosition.getColumn()) {
        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i), null));;
      }
    }
    //moves.add(new ChessMove(myPosition, new ChessPosition(2, 5), null));
    System.out.println("moves: " + moves);
    return moves;
  }
}
