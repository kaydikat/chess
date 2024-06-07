package ui;

import ResponseException.ResponseException;
import server_facade.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private State state = State.PRE_LOGIN;
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;

    public ChessClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public String help() {
        return "Commands:\n" +
                "  register <USERNAME> <PASSWORD> <EMAIL> - creates an account\n" +
                "  login <USERNAME> <PASSWORD> - to play chess\n" +
                "  help - show this message\n" +
                "  quit - exit the program\n";
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
                case "create" -> create();
                case "list" -> list();
                case "join" -> join(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ClientException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ClientException {
        return null;
    }
    public String register(String... params) throws ClientException, ResponseException {
        if (params.length != 3) {
            throw new ClientException("register requires 3 parameters: <USERNAME> <PASSWORD> <EMAIL>");
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];

        try {
        server.register(username, password, email);
        return String.format("Registered as %s", username);
        } catch (ResponseException e) {
            return e.getMessage();
        }
    }
    public String logout() throws ClientException {
        return null;
    }
    public String create() throws ClientException {
        return null;
    }
    public String list() throws ClientException {
        return null;
    }
    public String join(String... params) throws ClientException {
        return null;
    }
    public String quit() {
        return null;
    }
}
