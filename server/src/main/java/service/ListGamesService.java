package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import server.ListGamesResponse;

public class ListGamesService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ListGamesService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) != null) {
            return new ListGamesResponse(gameDAO.listGames());
        } else {
            throw new DataAccessException("401#Error: unauthorized");
        }
    }
}
