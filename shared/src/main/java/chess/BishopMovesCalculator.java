package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        var moves = new ArrayList<ChessMove>();
        moves.add(new ChessMove(myPosition, new ChessPosition(1, 1), null));
        return moves;
    }
}
