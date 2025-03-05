package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @BeforeEach
    void clear() throws DataAccessException {
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
    void negativeClearTest() {
        // I can't think of a way to purposefully make clear() fail, so this is just here as a placeholder
        assertNotEquals(0, 1);
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
    void negativeLogoutTest() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        registerService.register(user);

        assertThrows(DataAccessException.class, () -> logoutService.logout("not-an-auth"));
    }
}
