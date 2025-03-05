package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Objects;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws DataAccessException {
        for (AuthData auth : authDAO.listAuths()) {
            if (Objects.equals(auth.authToken(), authToken)) {
                authDAO.deleteAuth(authToken);
                return;
            }
        }
        throw new DataAccessException("401#Error: unauthorized");
    }
}
