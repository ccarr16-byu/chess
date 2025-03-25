package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;
import model.LoginRequest;
import model.LoginResponse;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResponse login(LoginRequest loginRequest) throws DataAccessException {
        if (userDAO.getUser(loginRequest.username()) == null) {
            throw new DataAccessException("401#Error: unauthorized");
        } else if (!BCrypt.checkpw(loginRequest.password(), userDAO.getUser(loginRequest.username()).password())) {
            throw new DataAccessException("401#Error: unauthorized");
        } else {
            AuthData authData = authDAO.createAuth(new AuthData("", loginRequest.username()));
            return new LoginResponse(loginRequest.username(), authData.authToken());
        }
    }
}
