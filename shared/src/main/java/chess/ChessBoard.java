package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares=new ChessPiece[8][8];

    public ChessBoard() {
    }

//    public void makeMove2(ChessMove move) throws InvalidMoveException {
//        chessGame.makeMove(move);
//    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start=move.getStartPosition();
        ChessPosition end=move.getEndPosition();
        ChessPiece piece=squares[start.getRow() - 1][start.getColumn() - 1];

        // Update the board by moving the piece
        squares[end.getRow() - 1][end.getColumn() - 1]=piece;
        squares[start.getRow() - 1][start.getColumn() - 1]=null;

        // Additional logic for capturing pieces, pawn promotion, etc.
}
    @Override
    protected Object clone() {
        ChessBoard clonedBoard = new ChessBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = this.squares[row][col];
                if (piece != null) {
                    clonedBoard.squares[row][col] = (ChessPiece) piece.clone();
                }
            }
        }
        return clonedBoard;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        // Check if the dimensions of the arrays are equal
        if (squares.length != that.squares.length || squares[0].length != that.squares[0].length) {
            return false;
        }
        // Check each element of the arrays
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                if (squares[i][j] == null) {
                    if (that.squares[i][j] != null) {
                        return false;
                    }
                } else {
                    if (!squares[i][j].equals(that.squares[i][j])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(squares);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        squares[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow() - 1;
        int col = position.getColumn() - 1;
        return squares[row][col];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares[0][0]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][1]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][2]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][3]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][4]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][5]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][6]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][7]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i=0; i < 8; i++) {
            squares[1][i]=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][i]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        for (int i=2; i < 6; i++) {
            for (int j=0; j < 8; j++) {
                squares[i][j]=null;
            }
        }
        squares[7][0]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][1]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][2]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][3]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][4]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][5]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][6]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][7]=new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }
}
