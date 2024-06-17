package client;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import responseexception.ResponseException;
import model.GameData;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.AuthData;
import ui.ChessBoardUi;
import websocket.ServerMessageObserver;
import static ui.EscapeSequences.*;

public class ChessClient {
    private State state = State.PRE_LOGIN;
    private final ServerFacade server;
    private final String serverUrl;
    private AuthData authData;
    private GameData gameData;
    private Map<Integer, Integer> gameMap = new HashMap<>();
    private final ServerMessageObserver observer;

    public ChessClient(String serverUrl, ServerMessageObserver observer) throws Exception {
        server = new ServerFacade(serverUrl, observer);
        this.serverUrl = serverUrl;
        this.observer = observer;
    }

    public String help() {
        if (state == State.PRE_LOGIN) {
            return "Commands:\n" +
                    "  register <USERNAME> <PASSWORD> <EMAIL> - creates an account\n" +
                    "  login <USERNAME> <PASSWORD> - to play chess\n" +
                    "  help - show this message\n" +
                    "  testws - test the WebSocket connection\n" +
                    "  quit - exit the program\n";
        } if (state == State.POST_LOGIN) {
            return "Commands:\n" +
                    "  logout - log out of the current session\n" +
                    "  create <NAME> - create a new game\n" +
                    "  list - list available games\n" +
                    "  join <GAME_ID> [WHITE][BLACK]- join a game\n" +
                    "  observe <GAME_ID> - observe a game\n" +
                    "  help - show this message\n" +
                    "  quit - exit the program\n";
        } else {
            return "Commands:\n" +
                    "  redraw - redraws the chess board\n" +
                    "  move <start> <end> - make a move\n" +
                    "  highlight <start> - highlight legal moves\n" +
                    "  resign - forfeit the game\n" +
                    "  leave - remove yourself from the game\n" +
                    "  help - show this message\n";
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "testws" -> testWebSocket();
                case "quit" -> quit();
                case "redraw" -> redraw();
                case "make move" -> makeMove(params);
                case "highlight" -> highlight(params);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (ClientException | ResponseException ex) {
            return ex.getMessage();
        }
    }


    public String login(String... params) throws ClientException {
        if (params.length != 2) {
            throw new ClientException("login requires 2 parameters: <USERNAME> <PASSWORD>");
        }
        String username=params[0];
        String password=params[1];

        try {
            authData = server.login(username, password);
            state = State.POST_LOGIN;
            System.out.print(SET_TEXT_COLOR_BLUE + help());
            return String.format("Logged in as %s with authToken: %s", username, authData.authToken());
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }
    public String register(String... params) throws ClientException, ResponseException {
        if (params.length != 3) {
            throw new ClientException("register requires 3 parameters: <USERNAME> <PASSWORD> <EMAIL>");
        }
        String username=params[0];
        String password=params[1];
        String email=params[2];

        try {
            authData = server.register(username, password, email);
            state = State.POST_LOGIN;
            System.out.print(SET_TEXT_COLOR_BLUE + help());
            return String.format("Registered as %s with authToken: %s", username, authData.authToken());
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }
    public String logout() throws ClientException, ResponseException {
        if (state == State.PRE_LOGIN) {
            throw new ClientException("You must be logged in to log out");
        } else {
            server.logout(authData);
            state=State.PRE_LOGIN;
            System.out.print(SET_TEXT_COLOR_BLUE + help());
            return "Logged out";
        }
    }
    public String create(String... params) throws ClientException {
        if (state == State.PRE_LOGIN) {
            throw new ClientException("You must be logged in to create game");
        } else {
            if (params.length != 1) {
                throw new ClientException("Create requires 1 parameter: <NAME>");
            }
            String gameName=params[0];

            try {
                gameData=server.create(authData.authToken(), gameName);
//                ChessBoard board = gameData.game().getBoard();
//                new ChessBoardUi(board);
                state=State.POST_LOGIN;
                return String.format("Created %s with gameID %d", gameName, gameData.gameID());
            } catch (ResponseException e) {
                return e.getMessage();
            }
        }
    }

    public String list() throws ClientException, ResponseException {
        if (state == State.PRE_LOGIN) {
            throw new ClientException("You must be logged in to list games");
        } else {
            Collection<GameData> games = server.list(authData.authToken());
            StringBuilder gameListBuilder = new StringBuilder();
            gameMap.clear();
            int index = 1;
            for (GameData game : games) {
                gameListBuilder.append(String.format("%d. %d %s: white = %s, black = %s\n",
                        index, game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername()));
                gameMap.put(index, game.gameID());
                index++;
            }
            return gameListBuilder.toString();
        }
    }
    public String join(String... params) throws ClientException {
        if (state == State.PRE_LOGIN) {
            throw new ClientException("You must be logged in to join game");
        } else {
            if (params.length != 2) {
                throw new ClientException("Create requires 2 parameters: <GAMEID> <COLOR>");
            }
            Integer gameNumber = Integer.valueOf(params[0]);
            String color = params[1];

            Integer gameID;
            if (gameNumber > 999) {
                gameID = gameNumber;
            } else {
                gameID=gameMap.get(gameNumber);
            }
            try {
                gameData=server.join(authData.authToken(), gameID, color);
                server.joinGame(authData.authToken(), gameID);
                state=State.GAME_STATE;
                System.out.print(SET_TEXT_COLOR_BLUE + help());
                return String.format("Joined %s as %s", gameData.gameName(), color);
            } catch (ResponseException e) {
                return e.getMessage();
            }
        }
    }
    public String observe(String... params) throws ClientException {
        String gameNumber = params[0];
        join(gameNumber, "observer");
        return "observer mode";
    }
    public String quit() {
        System.exit(0);
        return "Goodbye!";
    }

    public String redraw() {
        return null;
    }

    public String makeMove(String... params) {
        if (params.length != 2) {
            return "Make move requires 2 parameters: <start> <end>";
        }
        ChessPosition startPosition = parsePosition(params[0]);
        ChessPosition endPosition = parsePosition(params[1]);
        ChessPiece.PieceType promotionPiece = null;
        server.makeMove(authData.authToken(), gameData.gameID(), startPosition, endPosition, promotionPiece);
        return "Move made from " + params[0] + " to " + params[1];
    }

    private ChessPosition parsePosition(String position) {
        Map<String, Integer> colMap = createColumnMap();
        String col = position.substring(0, 1);
        int row = Integer.parseInt(position.substring(1));
        return new ChessPosition(row, colMap.get(col));
    }

    private Map<String, Integer> createColumnMap() {
        Map<String, Integer> colMap = new HashMap<>();
        char letter = 'a';
        for (int i = 1; i <= 8; i++) {
            colMap.put(String.valueOf(letter), i);
            letter++;
        }
        return colMap;
    }
    public String highlight(String... params) {
        return null;
    }
    public String resign() {
        return null;
    }
    public String leave() throws ResponseException {
        server.leaveGame(authData.authToken(), gameData.gameID());
        state = State.POST_LOGIN;
        return String.format("Left game %s", gameData.gameName());
    }

    public String testWebSocket() {
        try {
            server.testWebSocket();
            return "WebSocket connection test passed!";
        } catch (Exception e) {
            return "WebSocket connection test failed: " + e.getMessage();
        }
    }
}
