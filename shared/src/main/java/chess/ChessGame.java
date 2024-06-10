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
    private static final boolean[][] legalMoves=new boolean[8][8];

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
    @Override
    protected Object clone() {
        ChessGame clonedGame = new ChessGame();
        clonedGame.setTeamTurn(this.teamTurn);
        clonedGame.setBoard((ChessBoard) this.chessBoard.clone());
        return clonedGame;
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
        ChessPiece piece = chessBoard.getPiece(startPosition);

        if (piece != null) {
            Collection<ChessMove> moves = piece.pieceMoves(chessBoard, startPosition);

            for (ChessMove move : moves) {
                ChessGame clonedGame = (ChessGame) this.clone();

                if (piece.getTeamColor() != TeamColor.WHITE) {
                    clonedGame.setTeamTurn(TeamColor.BLACK);
                }

                try {
                    // Make the move on the cloned game
                    clonedGame.makeMove(move);
                    if (piece.getTeamColor() != TeamColor.WHITE) {
                        clonedGame.setTeamTurn(TeamColor.BLACK);
                    }

                    // If the move doesn't result in the team being in check, add it to valid moves
                    if (!clonedGame.isInCheck(clonedGame.teamTurn)) {
                        validMoves.add(move);
                    }
                } catch (InvalidMoveException e) {
                    // Do nothing
                }
            }
        }

        for (ChessMove move : validMoves) {
            legalMoves[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1]=true;
        }
        return validMoves;
    }
    public static boolean[][] getLegalMoves(){
        return legalMoves;
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
            chessBoard.addPiece(end, null);
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
                            return true;
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row + 1, col + 1));

                    for (ChessMove move : moves) {
                        try {

                            ChessGame clonedGame = (ChessGame) this.clone();
                            clonedGame.makeMove(move);

                            // If the move gets the king out of check, it's not checkmate
                            if (!clonedGame.isInCheck(teamColor)) {
                                return false;
                            }
                        } catch (InvalidMoveException e) {
                            // Do nothing
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamTurn != teamColor) {
            return false;
        }

        // If the team is in check, it's not stalemate
        if (isInCheck(teamColor)) {
            return false;
        }

        // Iterate through each square on the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));

                // Check if the piece belongs to the specified team
                if (piece != null && piece.getTeamColor() == teamColor) {
                    // Get valid moves for the piece
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row + 1, col + 1));

                    // If there are valid moves for any piece, it's not stalemate
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // If no piece has any valid moves, it's stalemate
        return true;
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
