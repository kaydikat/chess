import chess.*;
import chess.ChessPiece;

import java.io.PrintStream;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}