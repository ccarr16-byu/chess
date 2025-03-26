package ui;

import java.util.Arrays;

import Server.ServerFacade;
import exception.ResponseException;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private int state = 0;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        if (state == 0) {
            return preLoginEval(input);
        } else if (state == 1) {
            return postLoginEval(input);
        } else {
            return "Game UI";
        }
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

    public String postLoginEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout(params);
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

    public String createGame(String... params) throws ResponseException {
        return "createGame";
    }

    public String listGames(String... params) throws ResponseException {
        return "listGames";
    }

    public String joinGame(String... params) throws ResponseException {
        return "joinGame";
    }

    public String observeGame(String... params) throws ResponseException {
        return "observeGame";
    }

    public String logout(String... params) throws ResponseException {
        return "logout";
    }

    public String help() {
        return "help";
    }
}
