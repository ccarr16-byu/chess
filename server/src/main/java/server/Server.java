package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import service.*;
import spark.*;

import java.lang.reflect.Field;

public class Server {


    private final GameDAO gameDAO = new MySQLGameDAO();
    private final ClearService clearService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final ListGamesService listGamesService;
    private final CreateGameService createGameService;
    private final JoinGameService joinGameService;

    public Server() throws DataAccessException {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        this.clearService = new ClearService(userDAO, authDAO, gameDAO);
        this.registerService = new RegisterService(userDAO, authDAO);
        this.loginService = new LoginService(userDAO, authDAO);
        this.logoutService = new LogoutService(authDAO);
        this.listGamesService = new ListGamesService(gameDAO, authDAO);
        this.createGameService = new CreateGameService(gameDAO, authDAO);
        this.joinGameService = new JoinGameService(gameDAO, authDAO);
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

    private Object login(Request request, Response response) {
        LoginRequest loginRequest = new Gson().fromJson(request.body(), LoginRequest.class);
        loginRequest = new LoginRequest(loginRequest.username(),BCrypt.hashpw(loginRequest.password(),
                BCrypt.gensalt()));
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

    private Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            logoutService.logout(authToken);
            return success(null, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
    }

    private Object listGames(Request request, Response response) {
        String authToken = request.headers("Authorization");
        try {
            ListGamesResponse listGamesResponse = listGamesService.listGames(authToken);
            return success(listGamesResponse, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
    }

    private Object createGame(Request request, Response response) {
        String authToken = request.headers("Authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(request.body(), CreateGameRequest.class);
        if (createGameRequest.gameName() == null) {
            return badRequest(response);
        }
        try {
            CreateGameResponse createGameResponse = createGameService.createGame(authToken, createGameRequest);
            return success(createGameResponse, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
    }

    private Object joinGame(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("Authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
        if (joinGameRequest.gameID() == null || joinGameRequest.playerColor() == null) {
            return badRequest(response);
        } else if (gameDAO.getGame(joinGameRequest.gameID()) == null) {
            return badRequest(response);
        }
        try {
            joinGameService.joinGame(authToken, joinGameRequest);
            return success(null, response);
        } catch (DataAccessException e) {
            return this.processError(e.getMessage(), response);
        } catch (Exception e) {
            return unexpectedError(e, response);
        }
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
