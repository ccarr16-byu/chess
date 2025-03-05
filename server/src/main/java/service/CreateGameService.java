package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import server.CreateGameRequest;
import server.CreateGameResponse;

public class CreateGameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public CreateGameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest createGameRequest) throws DataAccessException {
        if (authDAO.getAuth(authToken) != null) {
            GameData newGame = gameDAO.createGame(new GameData(0, null, null,
                    createGameRequest.gameName(), new ChessGame()));
            return new CreateGameResponse(newGame.gameID());
        } else {
            throw new DataAccessException("401#Error: unauthorized");
        }
    }
}
