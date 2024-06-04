package dataaccess.gamedaos;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDaoInMemory extends AbstractGameDao {
  private static GameDaoInMemory instance;
  private final Map<Integer, GameData> games = new HashMap<>();

  private GameDaoInMemory() {}

  public static GameDaoInMemory getInstance() {
    if (instance == null) {
      instance = new GameDaoInMemory();
    }
    return instance;
  }

  @Override
  public Integer createGameID(GameData game) {
    int newGameID = 1000 + random.nextInt(9000);
    games.put(newGameID, game);
    return newGameID;
  }

  @Override
  public GameData getGame(Integer gameID) throws DataAccessException {
    if (!games.containsKey(gameID)) {
      throw new DataAccessException("Game not found");
    }
    return games.get(gameID);
  }

  @Override
  public Collection<GameData> listGames() {
    return games.values();
  }

  @Override
  protected void updateGameColor(Integer gameID, String whiteUsername, String blackUsername) {
    GameData game = games.get(gameID);
    game = new GameData(gameID, whiteUsername, blackUsername, game.gameName(), game.game());
    games.put(gameID, game);
  }

  @Override
  public void clear() {
    games.clear();
  }
}