package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.*;
import spark.*;

import java.lang.reflect.Field;

public class Server {
    private final ClearService clearService;
    private final RegisterService registerService;
    private final LoginService loginService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.registerService = new RegisterService(userDAO, authDAO);
        this.loginService = new LoginService(userDAO, authDAO);
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

    private Object clear(Request request, Response response) {
        try {
            clearService.clear();
            return success(null, response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
    }

    private Object register(Request request, Response response) {
        UserData userData = new Gson().fromJson(request.body(), UserData.class);
        if (!validateInputs(userData)) {
            return badRequest(response);
        }
        try {
            LoginResponse registerResponse = registerService.register(userData);
            return this.success(registerResponse, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
    }

    private Object login(Request request, Response response) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        if (!validateInputs(loginRequest)) {
            return badRequest(response);
        }
        try {
            LoginResponse loginResponse = loginService.login(loginRequest);
            return this.success(loginResponse, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }

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

    private Object processError(String message, Response response) {
        String[] messageArray = message.split("#");
        response.status(Integer.parseInt(messageArray[0]));
        ErrorResponse errorResponse = new ErrorResponse(messageArray[1]);
        return new Gson().toJson(errorResponse);
    }

    private Object unexpectedError(Exception e, Response response) {
        String errorString = "500#Error: " + e.getMessage();
        return this.processError(errorString, response);
    }

    private Object badRequest(Response response) {
        return this.processError("400#Error: bad request", response);
    }

    private Object success(Object requestOutput, Response response) {
        response.status(200);
        return new Gson().toJson(requestOutput);
    }

    private Boolean validateInputs(Object requestObject) {
        Class<?> requestClass = requestObject.getClass();
        Field[] fields = requestClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(requestObject) == null) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
