package ui;

public class ChessClient {
    private final String serverUrl;
    private final Repl repl;

    public ChessClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public String help() {
        return "Commands:\n" +
                "  help - show this message\n" +
                "  quit - exit the program\n" +
                "  list - list all pets\n" +
                "  add <name> <species> - add a pet\n" +
                "  remove <name> - remove a pet\n" +
                "  update <name> <species> - update a pet\n";
    }

    public String eval(String line) {
        if (line.equals("help")) {
            return help();
        } else if (line.equals("quit")) {
            return "Goodbye!";
        } else {
            return "Unknown command: " + line + "\n";
        }
    }
}
