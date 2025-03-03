package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import server.RegisterResponse;


public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResponse register(UserData userData) throws DataAccessException {
        if (userDAO.getUser(userData.username()) == null) {
            userDAO.createUser(userData);
            AuthData authData = authDAO.createAuth(new AuthData("", userData.username()));
            return new RegisterResponse(userData.username(), authData.authToken());
        } else {
            throw new DataAccessException("Unable to register");
        }
    }
}
