package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear() throws DataAccessException;
}
