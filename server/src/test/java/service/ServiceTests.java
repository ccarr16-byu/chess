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
    static final UserDAO userDAO = new MemoryUserDAO();
    static final AuthDAO authDAO = new MemoryAuthDAO();
    static final GameDAO gameDAO = new MemoryGameDAO();

    static final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    static final RegisterService registerService = new RegisterService(userDAO, authDAO);
    static final LoginService loginService = new LoginService(userDAO, authDAO);
    static final LogoutService logoutService = new LogoutService(authDAO);
    static final ListGamesService listGamesService = new ListGamesService(gameDAO, authDAO);
    static final CreateGameService createGameService = new CreateGameService(gameDAO, authDAO);
    static final JoinGameService joinGameService = new JoinGameService(gameDAO, authDAO);

    @BeforeEach
    void clear() {
        clearService.clear();
    }

    @Test
    void positiveRegisterTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        registerService.register(user);

        var users = userDAO.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(user));
    }

    @Test
    void negativeRegisterTest() throws DataAccessException {
        UserData user1 = new UserData("user", "password", "email");
        registerService.register(user1);
        UserData user2 = new UserData("user", "password", "email");

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> registerService.register(user2));
        assertEquals("403#Error: already taken", thrown.getMessage());
    }

    @Test
    void positiveClearTest() throws DataAccessException {
        userDAO.createUser(new UserData("username", "password", "email"));
        authDAO.createAuth(new AuthData("0", "username"));
        gameDAO.createGame(new GameData(1, "white", "black", "game",
                new ChessGame()));

        clearService.clear();
        assertEquals(0, userDAO.listUsers().size());
        assertEquals(0, authDAO.listAuths().size());
        assertEquals(0, gameDAO.listGames().size());
    }

    @Test
    void positiveLoginTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        registerService.register(user);
        LoginRequest loginRequest = new LoginRequest(user.username(), user.password());

        assertDoesNotThrow(() -> loginService.login(loginRequest));
    }

    @Test
    void negativeLoginTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        registerService.register(user);
        LoginRequest loginRequest = new LoginRequest(user.username(), null);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> loginService.login(loginRequest));
        assertEquals("401#Error: unauthorized", thrown.getMessage());
    }

    @Test
    void positiveLogoutTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        LoginResponse loginResponse = registerService.register(user);
        String authToken = loginResponse.authToken();

        assertDoesNotThrow(() -> logoutService.logout(authToken));
    }

    @Test
    void negativeLogoutTest() {
        assertThrows(DataAccessException.class, () -> logoutService.logout("not-an-auth"));
    }

    @Test
    void positiveListGamesTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = registerService.register(user).authToken();
        gameDAO.createGame(new GameData(0, null, null, "game",
                new ChessGame()));

        assertDoesNotThrow(() -> listGamesService.listGames(authToken));
        assertEquals(1, listGamesService.listGames(authToken).games().size());
    }

    @Test
    void negativeListGamesTest() {
        assertThrows(DataAccessException.class, () -> listGamesService.listGames("not-an-auth"));
    }

    @Test
    void positiveCreateGameTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = registerService.register(user).authToken();

        assertDoesNotThrow(() -> createGameService.createGame(authToken, new CreateGameRequest("new-game")));
        assertEquals(1, gameDAO.listGames().size());
    }

    @Test
    void negativeCreateGameTest() {
        assertThrows(DataAccessException.class, () -> createGameService.createGame("not-an-auth", new CreateGameRequest("new-game")));
    }

    @Test
    void positiveJoinGameTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        String authToken = registerService.register(user).authToken();
        int gameID = createGameService.createGame(authToken, new CreateGameRequest("new-game")).gameID();
        joinGameService.joinGame(authToken, new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID));

        assertEquals("username", gameDAO.getGame(gameID).whiteUsername());
    }

    @Test
    void negativeJoinGameTest() throws DataAccessException {
        UserData user1 = new UserData("user1", "password", "email");
        String authToken1 = registerService.register(user1).authToken();
        int gameID = createGameService.createGame(authToken1, new CreateGameRequest("new-game")).gameID();
        joinGameService.joinGame(authToken1, new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID));
        UserData user2 = new UserData("user2", "password", "email");
        String authToken2 = registerService.register(user2).authToken();

        assertThrows(DataAccessException.class, () -> joinGameService.joinGame(authToken2,
                new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID)));
        assertEquals("user1", gameDAO.getGame(gameID).whiteUsername());
    }
}
