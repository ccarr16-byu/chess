package ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import server.ServerFacade;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static chess.ChessGame.TeamColor.WHITE;

public class Client {
    private final ServerFacade server;
    private final NotificationHandler notificationHandler;
    private final String serverUrl;
    private WebSocketFacade ws;
    public int state = 0;
    private String authToken;
    private HashMap<Integer, GameData> gameMap;
    public int currentGame;

    public Client(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        if (state == 0) {
            return preLoginEval(input);
        } else if (state == 1) {
            return postLoginEval(input);
        } else if (state == 2) {
            return observationEval(input);
        } else {
            return inGameEval(input);
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

    public String observationEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "redraw" -> redraw();
                case "highlight" -> highlight();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String inGameEval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move();
                case "resign" -> resign();
                case "highlight" -> highlight();
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
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Invalid game number.";
            }
            ChessGame.TeamColor team;
            if (Objects.equals(params[1], "white")) {
                team = WHITE;
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
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.connect(this.authToken, game.gameID());
            this.currentGame = game.gameID();
            ChessBoardUI.drawChessBoard(team);
            this.state = 3;
            return String.format("Successfully joined game '%s'.\n", game.gameName());
        } else {
            return "Missing parameters.";
        }
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Invalid game number.";
            }
            if (this.gameMap == null) {
                return "List games first.";
            }
            GameData game = this.gameMap.getOrDefault(gameNumber, null);
            if (game == null) {
                return "Invalid game number.";
            }
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.connect(this.authToken, game.gameID());
            this.currentGame = game.gameID();
            this.state = 2;
            ChessBoardUI.drawChessBoard(WHITE);
            return "Game drawn.";
        } else {
            return "Missing parameters.";
        }
    }

    public String logout() throws ResponseException {
        if (state == 1) {
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

    public String redraw() throws ResponseException {
        return "redraw placeholder";
    }

    public String leave() throws ResponseException {
        try {
            ws.leave(this.authToken, currentGame);
        } catch (ResponseException ex) {
            return "Inavlid request.";
        }
        this.state = 1;
        return "Left game.";
    }

    public String move() throws ResponseException {
        return "move placeholder";
    }

    public String resign() throws ResponseException {
        return "resign placeholder";
    }

    public String highlight() throws ResponseException {
        return "highlight placeholder";
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
        } else if (state == 2) {
            return """
                    redraw\u001b[38;5;242m - redraw the chessboard
                    \u001b[38;5;12mleave\u001b[38;5;242m - leave the game
                    \u001b[38;5;12mhighlight <PIECE LOCATION>\u001b[38;5;242m - highlight valid a piece's moves
                    \u001b[38;5;12mhelp\u001b[38;5;242m - list possible commands
                   """;
        } else {
            return """
                    redraw\u001b[38;5;242m - redraw the chessboard
                    \u001b[38;5;12mleave\u001b[38;5;242m - leave the game
                    \u001b[38;5;12mmove <STARTING SQUARE> <ENDING SQUARE>\u001b[38;5;242m - make a move
                    \u001b[38;5;12mresign\u001b[38;5;242m - forfeit the game
                    \u001b[38;5;12mhighlight <PIECE LOCATION>\u001b[38;5;242m - highlight valid a piece's moves
                    \u001b[38;5;12mhelp\u001b[38;5;242m - list possible commands
                   """;
        }
    }
}
