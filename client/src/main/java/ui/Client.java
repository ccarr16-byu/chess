package ui;

import java.util.Arrays;

import Server.ServerFacade;
import exception.ResponseException;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String preLoginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        return "login";
    }

    public String register(String... params) throws ResponseException {
        return "register";
    }

    public String help() {
        return "help";
    }
}
