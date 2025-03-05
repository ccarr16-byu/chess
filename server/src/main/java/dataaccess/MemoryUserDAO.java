package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
    public UserData getUser(String username) {
        return users.getOrDefault(username, null);
    }

    @Override
    public Collection<UserData> listUsers() {
        List<UserData> listOfUsers = new ArrayList<>();
        users.forEach((id, game) -> listOfUsers.add(game));
        return listOfUsers;
    }

    @Override
    public void clear() {
        users.clear();
    }
}
