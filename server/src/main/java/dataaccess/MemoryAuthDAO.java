package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        String newToken = UUID.randomUUID().toString();
        authData = new AuthData(newToken, authData.username());
        auths.put(newToken, authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (auths.containsKey(authToken)) {
            return auths.get(authToken);
        } else {
            throw new DataAccessException("Unauthorized");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        auths.clear();
    }
}
