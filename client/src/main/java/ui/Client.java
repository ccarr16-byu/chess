package ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import chess.*;
import server.ServerFacade;
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
    public ChessGame.TeamColor currentTeam;
    public ChessGame currentGameState;
    public ChessMove lastMove;


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
                case "highlight" -> highlight(params);
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
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
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
            this.currentTeam = team;
            this.currentGame = game.gameID();
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
            this.currentTeam = WHITE;
            this.currentGame = game.gameID();
            this.state = 2;
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
        if (lastMove != null) {
            ChessBoardUI.drawChessBoard(currentTeam, currentGameState.getBoard(), 1, lastMove, null,
                    null);
        } else {
            ChessBoardUI.drawChessBoard(currentTeam, currentGameState.getBoard(), 0, null, null,
                    null);
        }
        return "";
    }

    public String leave() throws ResponseException {
        try {
            ws.leave(this.authToken, currentGame);
        } catch (ResponseException ex) {
            return "Invalid request.";
        }
        this.state = 1;
        return "Left game.";
    }

    public String move(String... params) throws ResponseException {
        if (params.length >= 2) {
            ChessPosition start;
            ChessPosition end;
            ChessPiece.PieceType promotionPiece;
            ChessMove chessMove;
            try {
                start = parseSquare(params[0]);
                end = parseSquare(params[1]);
                if (params.length >= 3) {
                    promotionPiece = parsePromotion(params[2]);
                } else {
                    promotionPiece = null;
                }
                chessMove = new ChessMove(start, end, promotionPiece);
            } catch (ResponseException ex) {
                return "Invalid parameters.";
            }
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            try {
                ws.makeMove(this.authToken, currentGame, chessMove);
            } catch (Exception e) {
                return "Invalid move.";
            }
            return "";
        } else {
            return "Missing parameters.";
        }
    }

    public String resign() throws ResponseException {
        try {
            ws.resign(this.authToken, currentGame);
            currentGameState.setIsGameOver();
            return "Game resigned.";
        } catch (ResponseException ex) {
            return "Invalid request.";
        }
    }

    public String highlight(String... params) throws ResponseException {
        if (params.length >= 1) {
            ChessPosition position;
            try {
                position = parseSquare(params[0]);
            } catch (ResponseException ex) {
                return "Invalid position.";
            }
            var validMoves = currentGameState.validMoves(position);
            ChessBoardUI.drawChessBoard(currentTeam, currentGameState.getBoard(), 2, null, position,
                    validMoves);
        } else {
            return "Missing parameters.";
        }
        return "";
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

    public ChessPosition parseSquare(String square) throws ResponseException {
        int row;
        int col = getCol(square);
        switch (square.charAt(1)) {
            case '1' -> row = 1;
            case '2' -> row = 2;
            case '3' -> row = 3;
            case '4' -> row = 4;
            case '5' -> row = 5;
            case '6' -> row = 6;
            case '7' -> row = 7;
            case '8' -> row = 8;
            default -> throw new ResponseException(403, "Invalid square.");
        }
        return new ChessPosition(row, col);
    }

    private static int getCol(String square) throws ResponseException {
        int col;
        if (square.length() < 2) {
            throw new ResponseException(403, "Invalid square.");
        }
        switch (square.charAt(0)) {
            case 'a', 'A' -> col = 1;
            case 'b', 'B' -> col = 2;
            case 'c', 'C' -> col = 3;
            case 'd', 'D' -> col = 4;
            case 'e', 'E' -> col = 5;
            case 'f', 'F' -> col = 6;
            case 'g', 'G' -> col = 7;
            case 'h', 'H' -> col = 8;
            default -> throw new ResponseException(403,"Invalid square.");
        }
        return col;
    }

    public ChessPiece.PieceType parsePromotion(String promotion) throws ResponseException {
        ChessPiece.PieceType promotionPiece;
        switch (promotion) {
            case "q", "Q" -> promotionPiece = ChessPiece.PieceType.QUEEN;
            case "r", "R" -> promotionPiece = ChessPiece.PieceType.ROOK;
            case "b", "B" -> promotionPiece = ChessPiece.PieceType.BISHOP;
            case "k", "K" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
            default -> throw new ResponseException(403, "Invalid promotion piece.");
        }
        return promotionPiece;
    }
}
