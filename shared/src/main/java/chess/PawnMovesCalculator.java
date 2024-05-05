package chess;

import chess.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
  public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
    var moves = new ArrayList<ChessMove>();
    if (pieceColor == ChessGame.TeamColor.WHITE) {
      ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
      ChessPiece enemyPieceRight = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
      ChessPiece enemyPieceLeft = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));

      // If the pawn is in the second row, it can move two squares forward
      if (myPosition.getRow() == 2) {
        // check if there is a piece in front of the pawn
        if (piece == null) {
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
          piece = board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()));
          if (piece == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
          }
        }
        // If there is an enemy piece to the right or left of the pawn, it can move diagonally to capture it
        if (enemyPieceRight != null) {
          if (enemyPieceRight.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
          }
        }
        if (enemyPieceLeft != null) {
          if (enemyPieceLeft.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
          }
        }
      } else {
        if (piece == null && myPosition.getRow() < 7) {
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
        }
        if (enemyPieceRight != null) {
          if (enemyPieceRight.getTeamColor() != pieceColor) {
            if (myPosition.getRow() == 7) {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            } else {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
            }
          }
        }
        if (enemyPieceLeft != null) {
          if (enemyPieceLeft.getTeamColor() != pieceColor) {
            if (myPosition.getRow() == 7) {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            } else {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
            }
          }
        }
        if (myPosition.getRow() == 7) {
          // If the pawn reaches the last row, it can be promoted to another piece
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
        }
      }
    } else {
      ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
      ChessPiece enemyPieceRight = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
      ChessPiece enemyPieceLeft = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
      if (myPosition.getRow() == 7) {
        if (piece == null) {
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
          piece = board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()));
          if (piece == null) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
          }
        }
        if (enemyPieceRight != null) {
          if (enemyPieceRight.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
          }
        }
        if (enemyPieceLeft != null) {
          if (enemyPieceLeft.getTeamColor() != pieceColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
          }
        }
      } else {
        if (piece == null && myPosition.getRow() > 2) {
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
        }
        if (enemyPieceRight != null) {
          if (enemyPieceRight.getTeamColor() != pieceColor) {
            if (myPosition.getRow() == 2) {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            } else {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
            }
          }
        }
        if (enemyPieceLeft != null) {
          if (enemyPieceLeft.getTeamColor() != pieceColor) {
            if (myPosition.getRow() == 2) {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            } else {
              moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
            }
          }
        }
        if (myPosition.getRow() == 2) {
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
          moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
        }
      }
    }
    return moves;
  }
}
