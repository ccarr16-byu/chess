package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void sendError(String message, String authToken) throws IOException {
        var c = connections.get(authToken);
        if (c.session.isOpen()) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message);
            c.send(new Gson().toJson(errorMessage));
        } else {
            connections.remove(c.authToken);
        }
    }

    public void loadGame(String authToken, ChessGame game, int method, ChessMove lastMove, ChessPosition position)
            throws IOException {
        var c = connections.get(authToken);
        if (c.session.isOpen()) {
            var loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game, method,
                    lastMove, position);
            c.send(new Gson().toJson(loadGameMessage));
        } else {
            connections.remove(c.authToken);
        }
    }

    public void broadcastMove(ChessGame game, int method, ChessMove lastMove) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c: connections.values()) {
            if (c.session.isOpen()) {
                loadGame(c.authToken, game, method, lastMove, null);
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            connections.remove(c.authToken);

        }
    }

    public void broadcast(String excludeAuthToken, NotificationMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(new Gson().toJson(notification, NotificationMessage.class));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
}
