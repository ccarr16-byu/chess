package dataaccess;

import model.AuthData;
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
    void positiveGetAuthTest() {

    }

    @Test
    void negativeGetAuthTest() {

    }

    @Test
    void positiveDeleteAuthTest() {

    }

    @Test
    void negativeDeleteAuthTest() {

    }

    @Test
    void positiveListAuthTest() {

    }

    @Test
    void negativeListAuthTest() {

    }

    @Test
    void positiveClearAuthTest() {

    }

    @Test
    void positiveCreateGameTest() {

    }

    @Test
    void negativeCreateGameTest() {

    }

    @Test
    void positiveGetGameTest() {

    }

    @Test
    void negativeGetGameTest() {

    }

    @Test
    void positiveListGameTest() {

    }

    @Test
    void negativeListGameTest() {

    }

    @Test
    void positiveUpdateGameTest() {

    }

    @Test
    void negativeUpdateGameTest() {

    }

    @Test
    void positiveClearGameTest() {

    }
}
