package dataaccess;
import model.GameData;
import java.util.List;

public interface GameDAO {
    void clear();
    GameData getGame(String gameID);
    void createGame(String gameName);
    void updateGame(GameData gameData);
    List<String> getGames();
}
