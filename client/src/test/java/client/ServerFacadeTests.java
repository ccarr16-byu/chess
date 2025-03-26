package client;

import exception.ResponseException;
import model.LoginResponse;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import Server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;


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

}
