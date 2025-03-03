package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        userData = new UserData(userData.username(), userData.password(), userData.email());
        if (users.containsKey(userData.username())) {
            throw new DataAccessException("Error: already taken");
        } else {
            users.put(userData.username(), userData);
            return userData;
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        } else {
            throw new DataAccessException("No such user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}
