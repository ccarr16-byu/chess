package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    private UserDAO getUserDAO() throws DataAccessException {
        UserDAO userDAO;
        userDAO = new MySQLUserDAO();
        userDAO.clear();
        return userDAO;
    }

    private AuthDAO getAuthDAO() throws DataAccessException {
        AuthDAO authDAO;
        authDAO = new MySQLAuthDAO();
        authDAO.clear();
        return authDAO;
    }

    private GameDAO getGameDAO() throws DataAccessException {
        GameDAO gameDAO;
        gameDAO = new MySQLGameDAO();
        gameDAO.clear();
        return gameDAO;
    }

    @Test
    void positiveCreateUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();

        UserData user = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> userDAO.createUser(user));
    }

    @Test
    void negativeCreateUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();

        UserData user = new UserData("username", null, "email");
        assertThrows(DataAccessException.class, () -> userDAO.createUser(user));
    }

    @Test
    void positiveGetUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();

        UserData user = new UserData("username", "password", "email");
        userDAO.createUser(user);
        assertDoesNotThrow(() -> userDAO.getUser(user.username()));
    }

    @Test
    void negativeGetUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();

        UserData user = new UserData("username", "password", "email");
        userDAO.createUser(user);
        assertNull(userDAO.getUser("wrong-username"));
    }

    @Test
    void positiveListUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();
        UserData user1 = new UserData("username1", "password1", "email1");
        UserData user2 = new UserData("username2", "password2", "email2");
        UserData user3 = new UserData("username3", "password3", "email3");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        ArrayList<UserData> list = (ArrayList<UserData>) userDAO.listUsers();
        assertEquals(3, list.size());
    }

    @Test
    void negativeListUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();
        UserData user1 = new UserData("username1", "password1", "email1");
        UserData user2 = new UserData("username2", "password2", "email2");
        UserData user3 = new UserData("username3", "password3", "email3");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        ArrayList<UserData> unhashedUsers = new ArrayList<>();
        unhashedUsers.add(user1);
        unhashedUsers.add(user2);
        unhashedUsers.add(user3);

        ArrayList<UserData> actual = (ArrayList<UserData>) userDAO.listUsers();
        assertNotEquals(unhashedUsers, actual);
    }

    @Test
    void positiveClearUserTest() throws DataAccessException {
        UserDAO userDAO = getUserDAO();
        UserData user1 = new UserData("username1", "password1", "email1");
        UserData user2 = new UserData("username2", "password2", "email2");
        UserData user3 = new UserData("username3", "password3", "email3");
        userDAO.createUser(user1);
        userDAO.createUser(user2);
        userDAO.createUser(user3);

        userDAO.clear();
        assertEquals(0, userDAO.listUsers().size());
    }

    @Test
    void positiveCreateAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth = new AuthData(null, "username");
        assertDoesNotThrow(() -> authDAO.createAuth(auth));
    }

    @Test
    void negativeCreateAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth = new AuthData(null, null);
        assertThrows(DataAccessException.class, () -> authDAO.createAuth(auth));
    }

    @Test
    void positiveGetAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth = new AuthData(null, "username");
        String authToken = authDAO.createAuth(auth).authToken();
        assertDoesNotThrow(() -> authDAO.getAuth(authToken));
    }

    @Test
    void negativeGetAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth = new AuthData(null, "username");
        assertNull(authDAO.getAuth("wrong-authToken"));
    }

    @Test
    void positiveDeleteAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth1 = new AuthData(null, "username1");
        AuthData auth2 = new AuthData(null, "username2");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);

        authDAO.deleteAuth(auth1.authToken());
        assertNull(authDAO.getAuth(auth1.authToken()));
    }

    @Test
    void negativeDeleteAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth = new AuthData(null, "username1");
        String authToken = authDAO.createAuth(auth).authToken();

        authDAO.deleteAuth("not-authToken");
        assertNotNull(authDAO.getAuth(authToken));
    }

    @Test
    void positiveListAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth1 = new AuthData(null, "username1");
        AuthData auth2 = new AuthData(null, "username2");
        AuthData auth3 = new AuthData(null, "username3");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);
        authDAO.createAuth(auth3);

        assertEquals(3, authDAO.listAuths().size());
    }

    @Test
    void negativeListAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth1 = new AuthData(null, "username1");
        AuthData auth2 = new AuthData(null, "username2");
        AuthData auth3 = new AuthData(null, "username3");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);
        authDAO.createAuth(auth3);

        ArrayList<AuthData> noTokensList = new ArrayList<>();
        noTokensList.add(auth1);
        noTokensList.add(auth2);
        noTokensList.add(auth3);

        ArrayList<AuthData> actual = (ArrayList<AuthData>) authDAO.listAuths();
        assertNotEquals(noTokensList, actual);
    }

    @Test
    void positiveClearAuthTest() throws DataAccessException {
        AuthDAO authDAO = getAuthDAO();

        AuthData auth1 = new AuthData(null, "username1");
        AuthData auth2 = new AuthData(null, "username2");
        AuthData auth3 = new AuthData(null, "username3");
        authDAO.createAuth(auth1);
        authDAO.createAuth(auth2);
        authDAO.createAuth(auth3);

        authDAO.clear();
        assertEquals(0, authDAO.listAuths().size());
    }

    @Test
    void positiveCreateGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, "myGame",
                new ChessGame());
        assertDoesNotThrow(() -> gameDAO.createGame(game));
    }

    @Test
    void negativeCreateGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, null, new ChessGame());
        assertThrows(DataAccessException.class, () -> gameDAO.createGame(game));
    }

    @Test
    void positiveGetGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, "myGame",
                new ChessGame());
        int gameID = gameDAO.createGame(game).gameID();
        assertDoesNotThrow(() -> gameDAO.getGame(gameID));
    }

    @Test
    void negativeGetGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, "myGame", new
                ChessGame());
        gameDAO.createGame(game);
        assertNull(gameDAO.getGame(10000));
    }

    @Test
    void positiveListGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game1 = new GameData(0, null, null, "myGame1",
                new ChessGame());
        GameData game2 = new GameData(0, null, null, "myGame2",
                new ChessGame());
        GameData game3 = new GameData(0, null, null, "myGame3",
                new ChessGame());
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);

        ArrayList<GameData> list = (ArrayList<GameData>) gameDAO.listGames();
        assertEquals(3, list.size());
    }

    @Test
    void negativeListGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game1 = new GameData(0, null, null, "myGame1",
                new ChessGame());
        GameData game2 = new GameData(0, null, null, "myGame2",
                new ChessGame());
        GameData game3 = new GameData(0, null, null, "myGame3",
                new ChessGame());
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);

        ArrayList<GameData> improperIDs = new ArrayList<>();
        improperIDs.add(game1);
        improperIDs.add(game2);
        improperIDs.add(game3);

        ArrayList<GameData> list = (ArrayList<GameData>) gameDAO.listGames();
        assertNotEquals(list, improperIDs);
    }

    @Test
    void positiveUpdateGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, "myGame",
                new ChessGame());
        int id = gameDAO.createGame(game).gameID();
        GameData alteredGame = new GameData(id, null, "blackUsername", "myGame",
                new ChessGame());
        gameDAO.updateGame(id, alteredGame);

        assertNotEquals(game, gameDAO.getGame(id));
    }

    @Test
    void negativeUpdateGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game = new GameData(0, null, null, "myGame",
                new ChessGame());
        int id = gameDAO.createGame(game).gameID();
        GameData alteredGame = new GameData(id, null, null, null,
                new ChessGame());

        assertThrows(DataAccessException.class, () -> gameDAO.updateGame(id, alteredGame));
    }

    @Test
    void positiveClearGameTest() throws DataAccessException {
        GameDAO gameDAO = getGameDAO();

        GameData game1 = new GameData(0, null, null, "myGame1",
                new ChessGame());
        GameData game2 = new GameData(0, null, null, "myGame2",
                new ChessGame());
        GameData game3 = new GameData(0, null, null, "myGame3",
                new ChessGame());
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);
        gameDAO.createGame(game3);

        gameDAO.clear();
        assertEquals(0, gameDAO.listGames().size());
    }
}
