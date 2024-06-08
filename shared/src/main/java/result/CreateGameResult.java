package result;

import chess.ChessGame;

public record CreateGameResult(Integer gameID, ChessGame game, String message) {}
