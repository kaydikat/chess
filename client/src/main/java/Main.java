import chess.*;
import chess.ChessPiece;
import client.Repl;


public class Main {

    public static void main(String[] args) throws Exception {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new Repl(serverUrl).run();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}