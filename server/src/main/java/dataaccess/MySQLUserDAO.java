package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.List;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        return null;
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<UserData> listUsers() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() {

    }
}
