package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
     private final ChessMove move;
     private final String start;
     private final String end;
     public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, String start, String end) {
         super(CommandType.MAKE_MOVE, authToken, gameID);
         this.move = move;
         this.start = start;
         this.end = end;
     }

        public ChessMove getMove() {
            return move;
        }

        public String getStart() {
            return start;
        }
        public String getEnd() {
            return end;
        }
}
