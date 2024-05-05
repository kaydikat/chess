package chess.pieces;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (type) {
            case PAWN -> {
                var pawnMovesCalculator = new PawnMovesCalculator();
                return pawnMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            case ROOK -> {
                var rookMovesCalculator = new RookMovesCalculator();
                return rookMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            case KNIGHT -> {
                var knightMovesCalculator = new KnightMovesCalculator();
                return knightMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            case BISHOP -> {
                var bishopMovesCalculator = new BishopMovesCalculator();
                return bishopMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            case QUEEN -> {
                var queenMovesCalculator = new QueenMovesCalculator();
                return queenMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            case KING -> {
                var kingMovesCalculator = new KingMovesCalculator();
                return kingMovesCalculator.calculateMoves(board, myPosition, pieceColor);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }
}

