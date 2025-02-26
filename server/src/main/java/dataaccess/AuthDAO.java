package dataaccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    AuthData getAuth(String authToken);
    void createAuth(AuthData authData);
    void deleteAuth(AuthData authData);
}
