package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();

    if (pieceColor == ChessGame.TeamColor.WHITE) {
      calculateWhitePawnMoves(board, myPosition, moves);
    } else {
      calculateBlackPawnMoves(board, myPosition, moves);
    }

    return moves;
  }

  private void calculateWhitePawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
    if (myPosition.getRow() < 8) {
      addForwardMove(board, myPosition, 1, moves);
      if (myPosition.getRow() == 2) {
        addDoubleForwardMove(board, myPosition, 1, moves);
      }
      addCaptureMoves(board, myPosition, 1, moves);
    }
  }

  private void calculateBlackPawnMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves) {
    if (myPosition.getRow() > 1) {
      addForwardMove(board, myPosition, -1, moves);
      if (myPosition.getRow() == 7) {
        addDoubleForwardMove(board, myPosition, -1, moves);
      }
      addCaptureMoves(board, myPosition, -1, moves);
    }
  }

  private void addForwardMove(ChessBoard board, ChessPosition myPosition, int direction, Collection<ChessMove> moves) {
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
    if (board.getPiece(newPosition) == null) {
      addMoveWithPromotionCheck(myPosition, newPosition, moves);
    }
  }

  private void addDoubleForwardMove(ChessBoard board, ChessPosition myPosition, int direction, Collection<ChessMove> moves) {
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
    if (board.getPiece(newPosition) == null) {
      ChessPosition doubleMovePosition = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());
      if (board.getPiece(doubleMovePosition) == null) {
        moves.add(new ChessMove(myPosition, doubleMovePosition, null));
      }
    }
  }

  private void addCaptureMoves(ChessBoard board, ChessPosition myPosition, int direction, Collection<ChessMove> moves) {
    addCaptureMove(board, myPosition, direction, 1, moves);  // Right capture
    addCaptureMove(board, myPosition, direction, -1, moves); // Left capture
  }

  private void addCaptureMove(ChessBoard board, ChessPosition myPosition, int rowDirection, int colDirection, Collection<ChessMove> moves) {
    ChessPosition newPosition = new ChessPosition(myPosition.getRow() + rowDirection, myPosition.getColumn() + colDirection);
    ChessPiece piece = board.getPiece(newPosition);
    if (piece != null && piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
      addMoveWithPromotionCheck(myPosition, newPosition, moves);
    }
  }

  private void addMoveWithPromotionCheck(ChessPosition fromPosition, ChessPosition toPosition, Collection<ChessMove> moves) {
    if (toPosition.getRow() == 1 || toPosition.getRow() == 8) {
      moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.QUEEN));
      moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.ROOK));
      moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.KNIGHT));
      moves.add(new ChessMove(fromPosition, toPosition, ChessPiece.PieceType.BISHOP));
    } else {
      moves.add(new ChessMove(fromPosition, toPosition, null));
    }
  }
}
