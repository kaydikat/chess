import chess.*;
import chess.ChessPiece;
import ui.ChessBoardUi;
import ui.Repl;
import server.Server;

import java.io.PrintStream;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class Main {
    private static Server server;
    public static void main(String[] args) {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        ChessBoardUi.drawBoard(System.out, "white");
        new Repl(serverUrl).run();
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}