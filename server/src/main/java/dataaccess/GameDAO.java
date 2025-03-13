package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData game) throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(int id, GameData updatedGame) throws DataAccessException;

    void clear() throws DataAccessException;
}
