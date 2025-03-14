package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.CreateGameRequest;
import server.JoinGameRequest;
import server.LoginRequest;
import server.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    static final UserDAO USER_DAO;

    static {
        try {
            USER_DAO = new MySQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static final AuthDAO AUTH_DAO;

    static {
        try {
            AUTH_DAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static final GameDAO GAME_DAO;

    static {
        try {
            GAME_DAO = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static final ClearService CLEAR_SERVICE = new ClearService(USER_DAO, AUTH_DAO, GAME_DAO);
    static final RegisterService REGISTER_SERVICE = new RegisterService(USER_DAO, AUTH_DAO);
    static final LoginService LOGIN_SERVICE = new LoginService(USER_DAO, AUTH_DAO);
    static final LogoutService LOGOUT_SERVICE = new LogoutService(AUTH_DAO);
    static final ListGamesService LIST_GAMES_SERVICE = new ListGamesService(GAME_DAO, AUTH_DAO);
    static final CreateGameService CREATE_GAME_SERVICE = new CreateGameService(GAME_DAO, AUTH_DAO);
    static final JoinGameService JOIN_GAME_SERVICE = new JoinGameService(GAME_DAO, AUTH_DAO);

    @BeforeEach
    void clear() throws DataAccessException {
        CLEAR_SERVICE.clear();
    }

    @Test
    void positiveRegisterTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        REGISTER_SERVICE.register(user);

        var users = USER_DAO.listUsers();
        assertEquals(1, users.size());
    }

    @Test
    void negativeRegisterTest() throws DataAccessException {
        UserData user1 = new UserData("user", "password", "email");
        REGISTER_SERVICE.register(user1);
        UserData user2 = new UserData("user", "password", "email");

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> REGISTER_SERVICE.register(user2));
        assertEquals("403#Error: already taken", thrown.getMessage());
    }

    @Test
    void positiveClearTest() throws DataAccessException {
        USER_DAO.createUser(new UserData("username", "password", "email"));
        AUTH_DAO.createAuth(new AuthData("0", "username"));
        GAME_DAO.createGame(new GameData(1, "white", "black", "game",
                new ChessGame()));

        CLEAR_SERVICE.clear();
        assertEquals(0, USER_DAO.listUsers().size());
        assertEquals(0, AUTH_DAO.listAuths().size());
        assertEquals(0, GAME_DAO.listGames().size());
    }

    @Test
    void positiveLoginTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        REGISTER_SERVICE.register(user);
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());

        assertDoesNotThrow(() -> LOGIN_SERVICE.login(loginRequest));
    }

    @Test
    void negativeLoginTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        REGISTER_SERVICE.register(user);
        LoginRequest loginRequest = new LoginRequest(user.username(), null);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> LOGIN_SERVICE.login(loginRequest));
        assertEquals("401#Error: unauthorized", thrown.getMessage());
    }

    @Test
    void positiveLogoutTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        LoginResponse loginResponse = REGISTER_SERVICE.register(user);
        String authToken = loginResponse.authToken();

        assertDoesNotThrow(() -> LOGOUT_SERVICE.logout(authToken));
    }

    @Test
    void negativeLogoutTest() {
        assertThrows(DataAccessException.class, () -> LOGOUT_SERVICE.logout("not-an-auth"));
    }

    @Test
    void positiveListGamesTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = REGISTER_SERVICE.register(user).authToken();
        GAME_DAO.createGame(new GameData(0, null, null, "game",
                new ChessGame()));

        assertDoesNotThrow(() -> LIST_GAMES_SERVICE.listGames(authToken));
        assertEquals(1, LIST_GAMES_SERVICE.listGames(authToken).games().size());
    }

    @Test
    void negativeListGamesTest() {
        assertThrows(DataAccessException.class, () -> LIST_GAMES_SERVICE.listGames("not-an-auth"));
    }

    @Test
    void positiveCreateGameTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = REGISTER_SERVICE.register(user).authToken();

        assertDoesNotThrow(() -> CREATE_GAME_SERVICE.createGame(authToken, new CreateGameRequest("new-game")));
        assertEquals(1, GAME_DAO.listGames().size());
    }

    @Test
    void negativeCreateGameTest() {
        assertThrows(DataAccessException.class, () -> CREATE_GAME_SERVICE.createGame("not-an-auth", new CreateGameRequest("new-game")));
    }

    @Test
    void positiveJoinGameTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = REGISTER_SERVICE.register(user).authToken();
        int gameID = CREATE_GAME_SERVICE.createGame(authToken, new CreateGameRequest("new-game")).gameID();
        JOIN_GAME_SERVICE.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID));

        assertEquals("username", GAME_DAO.getGame(gameID).whiteUsername());
    }

    @Test
    void negativeJoinGameTest() throws DataAccessException {
        UserData user1 = new UserData("user1", "password", "email");
        String authToken1 = REGISTER_SERVICE.register(user1).authToken();
        int gameID = CREATE_GAME_SERVICE.createGame(authToken1, new CreateGameRequest("new-game")).gameID();
        JOIN_GAME_SERVICE.joinGame(authToken1, new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID));
        UserData user2 = new UserData("user2", "password", "email");
        String authToken2 = REGISTER_SERVICE.register(user2).authToken();

        assertThrows(DataAccessException.class, () -> JOIN_GAME_SERVICE.joinGame(authToken2,
                new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID)));
        assertEquals("user1", GAME_DAO.getGame(gameID).whiteUsername());
    }
}
