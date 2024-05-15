package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard chessBoard;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.chessBoard = new ChessBoard();
        this.chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // Iterate through each square on the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));

                // Check if the square contains a piece of the current team
                if (piece != null && piece.getTeamColor() == teamTurn) {
                    // Get valid moves for the piece
                    Collection<ChessMove> moves = piece.pieceMoves(chessBoard, new ChessPosition(row + 1, col + 1));

                    // Clone the board and apply each move to check if it leads to being in check
                    for (ChessMove move : moves) {
                        ChessBoard clonedBoard =(ChessBoard) chessBoard.clone(); // Assuming you have implemented the clone method
                      // Apply the move to the cloned board
                      clonedBoard.makeMove(move);

                      // Check if the king is in check after applying the move
                      if (!isInCheck(teamTurn)) {
                          // Move doesn't result in check, so add it to valid moves
                          validMoves.add(move);
                      }
                    }
                }
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();
        ChessPiece piece = chessBoard.getPiece(start);

        if (piece == null) {
            throw new InvalidMoveException();
        }

        // Check if it's the current team's turn
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = piece.pieceMoves(chessBoard, start);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException();
        }
        ChessPiece opponentPiece = chessBoard.getPiece(end);
        if (opponentPiece != null && opponentPiece.getTeamColor() != teamTurn) {
            // Capture opponent's piece
            chessBoard.addPiece(start, null);
        }
        chessBoard.makeMove(move);
        if (promotionPiece != null && piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            // Check if the pawn reaches the opposite end of the board
            if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8) ||
                    (piece.getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1)) {
                // Promote the pawn
                chessBoard.addPiece(end, new ChessPiece(piece.getTeamColor(), promotionPiece));
            }
        }
        if (isInCheck(teamTurn)) {
            throw new InvalidMoveException();
        }
        teamTurn = (teamTurn == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(chessBoard, new ChessPosition(row+1, col+1));
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true; // King is in check
                        }
                    }
                }
            }
        }
        return false;
    }
    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return new ChessPosition(row + 1, col + 1); // Found the king's position
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }
}
