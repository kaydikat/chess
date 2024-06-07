package ui;

import java.util.Arrays;

public class ChessClient {
    private State state = State.PRE_LOGIN;
    private final String serverUrl;
    private final Repl repl;

    public ChessClient(String serverUrl, Repl repl) {
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
    public String register(String... params) throws ClientException {
        return null;
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
