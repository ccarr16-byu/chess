package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer, ConnectionHandler> connectionHandlers;
    private  ConnectionHandler connections;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.connectionHandlers = new HashMap<>();
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, ResponseException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        if (authDAO.getAuth(userGameCommand.getAuthToken()) == null) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "Error: unauthorized.");
            session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
            return;
        }
        if (gameDAO.getGame(userGameCommand.getGameID()) == null) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "Error: invalid game.");
            session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
            return;
        }
        if (connectionHandlers.containsKey(userGameCommand.getGameID())) {
            connections = connectionHandlers.get(userGameCommand.getGameID());
        } else {
            connections = new ConnectionHandler();
            connectionHandlers.put(userGameCommand.getGameID(), connections);
        }
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand, session);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                String username = authDAO.getAuth(makeMoveCommand.getAuthToken()).username();
                if (!Objects.equals(username, gameDAO.getGame(makeMoveCommand.getGameID()).blackUsername()) &&
                        !Objects.equals(username, gameDAO.getGame(makeMoveCommand.getGameID()).whiteUsername())) {
                    var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: not a player.");
                    session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
                    return;
                }
                try {
                    makeMove(makeMoveCommand);
                } catch (Exception ex) {
                    var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: invalid move.");
                    session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
                }
            }
            case LEAVE -> leave(userGameCommand);
            case RESIGN -> {
                String username = authDAO.getAuth(userGameCommand.getAuthToken()).username();
                if (!Objects.equals(username, gameDAO.getGame(userGameCommand.getGameID()).blackUsername()) &&
                        !Objects.equals(username, gameDAO.getGame(userGameCommand.getGameID()).whiteUsername())) {
                    var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                            "Error: not a player.");
                    session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
                    return;
                }
                resign(userGameCommand);
            }
        }
    }

    private void connect(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        connections.add(userGameCommand.getAuthToken(), session);
        connections.loadGame(userGameCommand.getAuthToken(), gameDAO.getGame(userGameCommand.getGameID()).gameState(),
                0, null,
                null);
        String joiningAs;
        if (Objects.equals(gameDAO.getGame(userGameCommand.getGameID()).whiteUsername(),
                authDAO.getAuth(userGameCommand.getAuthToken()).username())) {
            joiningAs = "the white player";
        } else if (Objects.equals(gameDAO.getGame(userGameCommand.getGameID()).blackUsername(),
                authDAO.getAuth(userGameCommand.getAuthToken()).username())) {
            joiningAs = "the black player";
        } else {
            joiningAs = "an observer";
        }
        String message = String.format("%s has joined the game as %s.",
                authDAO.getAuth(userGameCommand.getAuthToken()).username(), joiningAs);
        var notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(userGameCommand.getAuthToken(), notificationMessage);
    }

    private void makeMove(MakeMoveCommand makeMoveCommand) throws DataAccessException, IOException {
        ChessMove chessMove = makeMoveCommand.getChessMove();
        GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
        ChessGame chessGame = gameData.gameState();
        if (chessGame.getGameOver()) {
            connections.sendError("Error: game is over.", makeMoveCommand.getAuthToken());
            return;
        }
        String username = authDAO.getAuth(makeMoveCommand.getAuthToken()).username();
        ChessGame.TeamColor team;
        if (Objects.equals(gameData.blackUsername(), username)) {
            team = ChessGame.TeamColor.BLACK;
        } else {
            team = ChessGame.TeamColor.WHITE;
        }
        if (chessGame.getTeamTurn() != team) {
            connections.sendError("Error: not your turn.", makeMoveCommand.getAuthToken());
            return;
        }
        try {
            chessGame.makeMove(chessMove);
            var updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), chessGame);
            gameDAO.updateGame(gameData.gameID(), updatedGameData);
            connections.broadcastMove(gameDAO.getGame(makeMoveCommand.getGameID()).gameState(), 1, chessMove);
            var startPosition = parseSquare(chessMove.getStartPosition());
            var endPosition = parseSquare(chessMove.getEndPosition());
            var movedPiece = chessGame.getBoard().getPiece(chessMove.getEndPosition());

            String message = String.format("%s moved %s %s to %s.",
                    authDAO.getAuth(makeMoveCommand.getAuthToken()).username(), startPosition, movedPiece, endPosition);
            var notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(makeMoveCommand.getAuthToken(), notificationMessage);
            if (updatedGameData.gameState().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "White is in checkmate. Black wins!");
                connections.broadcast(null, notification);
            } else if (updatedGameData.gameState().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "Black is in checkmate. White wins!");
                connections.broadcast(null, notification);
            } else if (updatedGameData.gameState().isInCheck(ChessGame.TeamColor.WHITE)) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "White is in check.");
                connections.broadcast(null, notification);
            } else if (updatedGameData.gameState().isInCheck(ChessGame.TeamColor.BLACK)) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "Black is in check.");
                connections.broadcast(null, notification);
            } else if (updatedGameData.gameState().isInStalemate(ChessGame.TeamColor.WHITE) ||
                    updatedGameData.gameState().isInStalemate(ChessGame.TeamColor.BLACK)) {
                var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        "Game is a stalemate.");
                connections.broadcast(null, notification);
            }
        } catch (InvalidMoveException e) {
            connections.sendError("Error: invalid move.", makeMoveCommand.getAuthToken());
        }
    }

    private void leave(UserGameCommand userGameCommand) throws DataAccessException, IOException {
        String authToken = userGameCommand.getAuthToken();
        Integer gameID = userGameCommand.getGameID();
        connections.remove(authToken);
        gameDAO.updateGame(userGameCommand.getGameID(), removePlayer(authToken, gameDAO.getGame(gameID)));
        String message = String.format("%s has left the game.",
                authDAO.getAuth(userGameCommand.getAuthToken()).username());
        var notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(userGameCommand.getAuthToken(), notificationMessage);
    }

    private void resign(UserGameCommand userGameCommand) throws DataAccessException, IOException {
        var game = gameDAO.getGame(userGameCommand.getGameID());
        if (game.gameState().getGameOver()) {
            connections.sendError("Error: game is over.", userGameCommand.getAuthToken());
            return;
        }
        game.gameState().setIsGameOver();
        gameDAO.updateGame(userGameCommand.getGameID(), game);
        String username = authDAO.getAuth(userGameCommand.getAuthToken()).username();
        String message = String.format("%s has resigned. The game is over.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(null, notification);
    }

    private GameData removePlayer(String authToken, GameData game)
            throws DataAccessException {
        String username = authDAO.getAuth(authToken).username();
        String whiteUser;
        String blackUser;
        if (Objects.equals(username, game.whiteUsername())) {
            whiteUser = null;
            blackUser = game.blackUsername();
        } else if (Objects.equals(username, game.blackUsername())) {
            whiteUser = game.whiteUsername();
            blackUser = null;
        } else {
            return game;
        }
        return new GameData(game.gameID(), whiteUser, blackUser, game.gameName(), game.gameState());
    }

    public String parseSquare(ChessPosition square) {
        String col = "";
        switch (square.getColumn()) {
            case 1 -> col = "a";
            case 2 -> col = "b";
            case 3 -> col = "c";
            case 4 -> col = "d";
            case 5 -> col = "e";
            case 6 -> col = "f";
            case 7 -> col = "g";
            case 8 -> col = "h";
        }
        return col + square.getRow();
    }
}
