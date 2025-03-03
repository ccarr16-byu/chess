package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    private int nextID = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        game = new GameData(nextID++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.gameState());
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        if (games.containsKey(id)) {
            return games.get(id);
        } else {
            throw new DataAccessException("No such game");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        List<GameData> listOfGames = new ArrayList<>();
        games.forEach((id, game) -> listOfGames.add(game));
        return listOfGames;
    }

    @Override
    public void updateGame(int id) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
