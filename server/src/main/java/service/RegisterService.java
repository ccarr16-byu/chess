package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import model.LoginResponse;


public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResponse register(UserData userData) throws DataAccessException {
        if (userDAO.getUser(userData.username()) == null) {
            userDAO.createUser(userData);
            AuthData authData = authDAO.createAuth(new AuthData("", userData.username()));
            return new LoginResponse(userData.username(), authData.authToken());
        } else {
            throw new DataAccessException("403#Error: already taken");
        }
    }
}
