package client;

import exception.ResponseException;
import model.LoginRequest;
import model.LoginResponse;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import Server.ServerFacade;

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
    public void negativeLogoutTest() throws ResponseException {
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
    public void negativeLoginTest() throws ResponseException {
        var loginRequest = new LoginRequest("fake-user", "fake-password");
        assertThrows(ResponseException.class, () -> serverFacade.login(loginRequest));
    }
}
