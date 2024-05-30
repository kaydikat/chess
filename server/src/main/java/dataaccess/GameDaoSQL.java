package dataaccess;

import model.GameData;

import java.util.Collection;

public class GameDaoSQL implements GameDao {
  public Integer createGameID(GameData game) throws DataAccessException {
    return null;
  }

  public GameData createGame(String gameName) throws DataAccessException {
    return null;
  }

  public GameData getGame(Integer gameID) throws DataAccessException {
    return null;
  }

  public Collection<GameData> listGames() {
    return null;
  }

  public void addColor(Integer gameID, String playerColor, String username) throws DataAccessException {

  }

  public void clear() {

  }
}
