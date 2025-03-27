package ui;

import java.util.Arrays;

import Server.ServerFacade;
import exception.ResponseException;
import model.UserData;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    public int state = 0;

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
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            String returnedUsername;
            try {
                var userData = new UserData(username, password, email);
                returnedUsername = server.register(userData).username();
            } catch (ResponseException ex) {
                return "Registration not successful.";
            }
            this.state = 1;
            return String.format("Successfully signed in as %s.", returnedUsername);
        } else {
            return "Missing parameters.";
        }
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
        if (state == 0) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL>\u001b[38;5;242m - to create an account
                    \u001b[38;5;12mlogin <USERNAME> <PASSWORD>\u001b[38;5;242m - to play chess
                    \u001b[38;5;12mquit\u001b[38;5;242m - done playing chess
                    \u001b[38;5;12mhelp\u001b[38;5;242m - list possible commands
                   """;
        } else if (state == 1) {
            return """
                    create <GAME NAME>\u001b[38;5;242m - to create an game
                    \u001b[38;5;12mlist\u001b[38;5;242m - to see available games
                    \u001b[38;5;12mjoin <GAME ID> <COLOR>\u001b[38;5;242m - join a game
                    \u001b[38;5;12mobserve <GAME ID>\u001b[38;5;242m - watch a game
                    \u001b[38;5;12mlogout\u001b[38;5;242m - back to menu
                    \u001b[38;5;12mquit\u001b[38;5;242m - done playing chess
                    \u001b[38;5;12mhelp\u001b[38;5;242m - list possible commands
                   """;
        } else {
            return "Game UI instructions placeholder";
        }
    }
}
