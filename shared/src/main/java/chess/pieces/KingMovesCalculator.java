package chess.pieces;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i == 0 && j == 0) {
          continue;
        }
        ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + j);
        if (newPosition.getRow() >= 1 && newPosition.getRow() <= 8 && newPosition.getColumn() >= 1 && newPosition.getColumn() <= 8) {
          ChessPiece piece=board.getPiece(newPosition);
          if (piece == null || piece.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(myPosition, newPosition, null));
          }
        }
      }
    }
    return moves;
  }
}
