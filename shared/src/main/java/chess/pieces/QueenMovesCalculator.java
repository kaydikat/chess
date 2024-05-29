package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
  private final BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator();
  private final RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();

  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();

    moves.addAll(bishopMovesCalculator.calculateMoves(board, myPosition, pieceColor));
    moves.addAll(rookMovesCalculator.calculateMoves(board, myPosition, pieceColor));

    return moves;
  }
}