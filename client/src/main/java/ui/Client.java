package ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import Server.ServerFacade;
import chess.ChessGame;
import exception.ResponseException;
import model.*;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    public int state = 0;
    private String authToken;
    private HashMap<Integer, GameData> gameMap;

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
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            var username = params[0];
            var password = params[1];
            String returnedUsername;
            String returnedAuthToken;
            try {
                var loginRequest = new LoginRequest(username, password);
                LoginResponse loginResponse = server.login(loginRequest);
                returnedUsername = loginResponse.username();
                returnedAuthToken = loginResponse.authToken();
            } catch (ResponseException ex) {
                return "Login not successful.";
            }
            this.state = 1;
            this.authToken = returnedAuthToken;
            return String.format("Successfully logged in as %s.", returnedUsername);
        } else {
            return "Missing parameters.";
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            String returnedUsername;
            String returnedAuthToken;
            try {
                var userData = new UserData(username, password, email);
                LoginResponse registerResponse = server.register(userData);
                returnedUsername = registerResponse.username();
                returnedAuthToken = registerResponse.authToken();
            } catch (ResponseException ex) {
                return "Registration not successful.";
            }
            this.state = 1;
            this.authToken = returnedAuthToken;
            return String.format("Successfully logged in as %s.", returnedUsername);
        } else {
            return "Missing parameters.";
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            var createGameRequest = new CreateGameRequest(params[0]);
            try {
                server.createGame(createGameRequest, this.authToken);
            } catch (ResponseException ex) {
                return "Game creation not successful.";
            }
            return String.format("Successfully created game '%s'.", createGameRequest.gameName());
        } else {
            return "Missing parameters.";
        }
    }

    public String listGames() throws ResponseException {
        ListGamesResponse listGamesResponse;
        try {
            listGamesResponse = server.listGames(this.authToken);
        } catch (ResponseException ex) {
            return "Unable to list games.";
        }
        var gamesList = listGamesResponse.games();
        if (gamesList.isEmpty()) {
            return "No games to list.";
        } else {
            int counter = 1;
            this.gameMap = new HashMap<>();
            StringBuilder returnString = new StringBuilder();
            for (var game : gamesList) {
                String whiteUsername;
                String blackUsername;
                if (game.whiteUsername() == null) {
                    whiteUsername = "Not taken";
                } else {
                    whiteUsername = game.whiteUsername();
                }
                if (game.blackUsername() == null) {
                    blackUsername = "Not taken";
                } else {
                    blackUsername = game.blackUsername();
                }
                returnString.append(String.format("%d: %s - White: %s, Black: %s\n", counter, game.gameName(),
                        whiteUsername, blackUsername));
                gameMap.put(counter, game);
                counter++;
            }
            return returnString.toString();
        }
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            int gameNumber = Integer.parseInt(params[0]);
            ChessGame.TeamColor team;
            if (Objects.equals(params[1], "white")) {
                team = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                team = ChessGame.TeamColor.BLACK;
            } else {
                return "Invalid team color.";
            }
            if (this.gameMap == null) {
                return "List games first.";
            }
            GameData game = this.gameMap.getOrDefault(gameNumber, null);
            if (game == null) {
                return "Invalid game number.";
            }
            var joinGameRequest = new JoinGameRequest(team, game.gameID());
            try {
                server.joinGame(joinGameRequest, this.authToken);
            } catch (ResponseException ex) {
                return "Unable to join game.";
            }
            ChessBoardUI.drawChessBoard(team);
            return String.format("Successfully joined game '%s'.\n", game.gameName());
        } else {
            return "Missing parameters.";
        }
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            int gameNumber = Integer.parseInt(params[0]);
            ChessBoardUI.drawChessBoard(ChessGame.TeamColor.WHITE);
            return "Game drawn.";
        } else {
            return "Missing parameters.";
        }
    }

    public String logout() throws ResponseException {
        if (state != 0) {
            try {
                server.logout(this.authToken);
            } catch (ResponseException ex) {
                return "Logout not successful.";
            }
            state = 0;
            this.authToken = "";
            return "Successfully logged out.";
        } else {
            return "Not logged in.";
        }
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
                    \u001b[38;5;12mjoin <GAME ID> [WHITE|BLACK]\u001b[38;5;242m - join a game
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
