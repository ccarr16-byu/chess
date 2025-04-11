package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionHandler connections = new ConnectionHandler();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        if (authDAO.getAuth(userGameCommand.getAuthToken()) == null) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "Error: unauthorized");
            session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
        }
        if (gameDAO.getGame(userGameCommand.getGameID()) == null) {
            var errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,
                    "Error: invalid game");
            session.getRemote().sendString(new Gson().toJson(errorMessage, ErrorMessage.class));
        }
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand, session);
            case MAKE_MOVE -> {
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove();
            }
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        connections.add(userGameCommand.getAuthToken(), session);
        connections.loadGame(userGameCommand.getAuthToken(), userGameCommand.getGameID());
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

    private void makeMove() {
    }

    private void leave() {
    }

    private void resign() {
    }
}
