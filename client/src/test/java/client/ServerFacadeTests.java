package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        serverFacade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    void clearServer() throws ResponseException { serverFacade.clear(); }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void positiveRegisterTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        LoginResponse registerResponse = serverFacade.register(userData);
        assertNotNull(registerResponse.authToken());
    }

    @Test
    public void positiveClearTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        serverFacade.register(userData);
        serverFacade.clear();
        assertDoesNotThrow(() -> serverFacade.register(userData));
    }

    @Test
    public void negativeRegisterTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        serverFacade.register(userData);
        assertThrows(ResponseException.class, () -> serverFacade.register(userData));
    }

    @Test
    public void positiveLogoutTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        serverFacade.logout(authToken);
        assertThrows(ResponseException.class, () -> serverFacade.logout(authToken));
    }

    @Test
    public void negativeLogoutTest() {
        assertThrows(ResponseException.class, () -> serverFacade.logout("not-an-auth"));
    }

    @Test
    public void positiveLoginTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        serverFacade.logout(authToken);
        var loginRequest = new LoginRequest(userData.username(), userData.password());
        assertDoesNotThrow(() -> serverFacade.login(loginRequest));
    }

    @Test
    public void negativeLoginTest() {
        var loginRequest = new LoginRequest("fake-user", "fake-password");
        assertThrows(ResponseException.class, () -> serverFacade.login(loginRequest));
    }

    @Test
    public void positiveCreateGameTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        var createGameRequest = new CreateGameRequest("gameName");
        assertDoesNotThrow(() -> serverFacade.createGame(createGameRequest, authToken));
    }

    @Test
    public void negativeCreateGameTest() {
        var createGameRequest = new CreateGameRequest("gameName");
        assertThrows(ResponseException.class, () -> serverFacade.createGame(createGameRequest, "bad-auth"));
    }

    @Test
    public void positiveListGameTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        var createGameRequest = new CreateGameRequest("gameName");
        serverFacade.createGame(createGameRequest, authToken);
        assertEquals(1, serverFacade.listGames(authToken).games().size());
    }

    @Test
    public void negativeListGameTest() {
        assertThrows(ResponseException.class, () -> serverFacade.listGames("bad-auth"));
    }

    @Test
    public void positiveJoinGameTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        var createGameRequest = new CreateGameRequest("gameName");
        int gameID = serverFacade.createGame(createGameRequest, authToken).gameID();
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
        assertDoesNotThrow(() -> serverFacade.joinGame(joinGameRequest, authToken));
    }

    @Test
    public void negativeJoinGameTest() throws ResponseException {
        UserData userData = new UserData("username", "password", "email");
        String authToken = serverFacade.register(userData).authToken();
        int badID = 10000;
        var joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.BLACK, badID);
        assertThrows(ResponseException.class, () -> serverFacade.joinGame(joinGameRequest, authToken));
    }
}
