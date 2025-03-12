package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.List;

public class MySQLAuthDao implements AuthDAO {
    @Override
    public AuthData createAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public Collection<AuthData> listAuths() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clear() {

    }
}
