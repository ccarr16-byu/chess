package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.ClearService;
import service.RegisterService;
import spark.*;

public class Server {
    private final ClearService clearService;
    private final RegisterService registerService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.registerService = new RegisterService(userDAO, authDAO);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);



        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request request, Response response) throws DataAccessException {
        clearService.clear();
        response.status(200);
        return "";
    }

    private Object register(Request request, Response response) throws DataAccessException {
        UserData userData = new Gson().fromJson(request.body(), UserData.class);
        RegisterResponse registerResponse = registerService.register(userData);
        response.status(200);
        return new Gson().toJson(registerResponse);
    }

    private Object login(Request request, Response response) throws DataAccessException {
        return null;
    }

    private Object logout(Request request, Response response) throws DataAccessException {
        return null;
    }

    private Object listGames(Request request, Response response) throws DataAccessException {
        return null;
    }

    private Object createGame(Request request, Response response) throws DataAccessException {
        return null;
    }

    private Object joinGame(Request request, Response response) throws DataAccessException {
        return null;
    }
}
