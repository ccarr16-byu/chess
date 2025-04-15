package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public JoinGameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        if (authDAO.getAuth(authToken) != null) {
            GameData game = gameDAO.getGame(joinGameRequest.gameID());
            if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
                throw new DataAccessException("403#Error: already taken");
            } else if (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                throw new DataAccessException("403#Error: already taken");
            } else {
                GameData updatedGame = this.addPlayer(authToken, joinGameRequest.playerColor(), game);
                gameDAO.updateGame(joinGameRequest.gameID(), updatedGame);
            }
        } else {
            throw new DataAccessException("401#Error: unauthorized");
        }
    }

    private GameData addPlayer(String authToken, ChessGame.TeamColor playerColor, GameData game)
            throws DataAccessException {
        String whiteUserName;
        String blackUserName;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            whiteUserName = authDAO.getAuth(authToken).username();
            blackUserName = game.blackUsername();
        } else {
            blackUserName = authDAO.getAuth(authToken).username();
            whiteUserName = game.whiteUsername();
        }
        return new GameData(game.gameID(), whiteUserName, blackUserName, game.gameName(), game.gameState());
    }
}
