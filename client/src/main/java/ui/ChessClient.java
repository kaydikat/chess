package ui;

import ResponseException.ResponseException;
import model.GameData;
import server_facade.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

import model.AuthData;

public class ChessClient {
    private State state = State.PRE_LOGIN;
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private AuthData authData;
    private GameData gameData;

    public ChessClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public String help() {
        if (state == State.PRE_LOGIN) {
            return "Commands:\n" +
                    "  register <USERNAME> <PASSWORD> <EMAIL> - creates an account\n" +
                    "  login <USERNAME> <PASSWORD> - to play chess\n" +
                    "  help - show this message\n" +
                    "  quit - exit the program\n";
        } else {
            return "Commands:\n" +
                    "  logout - log out of the current session\n" +
                    "  create <NAME> - create a new game\n" +
                    "  list - list available games\n" +
                    "  join <GAME_ID> [WHITE][BLACK]- join a game\n" +
                    "  observe <ID> - observe a game\n" +
                    "  help - show this message\n" +
                    "  quit - exit the program\n";
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
                case "quit" -> quit();
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
            System.out.print(help());
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
            System.out.print(help());
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
                state=State.POST_LOGIN;
                System.out.print(help());
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
            int index = 1;
            for (GameData game : games) {
                gameListBuilder.append(String.format("%d. %s\n", index++, game));
            }
            return gameListBuilder.toString();
        }
    }
    public String join(String... params) throws ClientException {
        if (state == State.PRE_LOGIN) {
            throw new ClientException("You must be logged in to join game");
        } else {
            if (params.length != 2) {
                throw new ClientException("Create requires 2 parameter: <GAMEID> <COLOR>");
            }
            Integer gameID = Integer.valueOf(params[0]);
            String color=params[1];

            try {
                gameData=server.join(authData.authToken(), gameID, color);
                System.out.print(help());
                return String.format("Joined %s as %s", gameData.gameName(), color);
            } catch (ResponseException e) {
                return e.getMessage();
            }
        }
    }
    public String observe(String... params) throws ClientException {
        return null;
    }
    public String quit() {
        System.exit(0);
        return "Goodbye!";
    }

}
