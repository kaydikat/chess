package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        var moves = new ArrayList<ChessMove>();
        for (int i = 1; myPosition.getRow() - i >= 1 && myPosition.getColumn() - i >= 1; i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i));
            if (piece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
                break; // Stop iteration if blocked by a piece of the opposite color
            } else {
                break; // Stop iteration if blocked by a piece of the same color
            }
        }

        // Iterate diagonally upwards and to the right
        for (int i = 1; myPosition.getRow() - i >= 1 && myPosition.getColumn() + i <= 8; i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i));
            if (piece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
                break; // Stop iteration if blocked by a piece of the opposite color
            } else {
                break; // Stop iteration if blocked by a piece of the same color
            }
        }

        // Iterate diagonally downwards and to the left
        for (int i = 1; myPosition.getRow() + i <= 8 && myPosition.getColumn() - i >= 1; i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i));
            if (piece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), null));
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), null));
                break; // Stop iteration if blocked by a piece of the opposite color
            } else {
                break; // Stop iteration if blocked by a piece of the same color
            }
        }

        // Iterate diagonally downwards and to the right
        for (int i = 1; myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8; i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i));
            if (piece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                break; // Stop iteration if blocked by a piece of the opposite color
            } else {
                break; // Stop iteration if blocked by a piece of the same color
            }
        }

        return moves;
    }
}
