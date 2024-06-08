package result;

import chess.ChessGame;

public record JoinGameResult(String whiteUsername, String blackUsername,
                             String gameName, ChessGame game, String message) {}
