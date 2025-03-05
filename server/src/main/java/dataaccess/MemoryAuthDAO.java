package dataaccess;

import model.AuthData;

import java.util.*;

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
    public AuthData getAuth(String authToken) {
        return auths.getOrDefault(authToken, null);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        List<AuthData> listOfAuths = new ArrayList<>();
        auths.forEach((id, game) -> listOfAuths.add(game));
        return listOfAuths;
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
