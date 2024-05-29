package dataaccess;

import model.GameData;
import java.util.Collection;

public interface GameDao {
  Integer createGameID(GameData game) throws DataAccessException;
  GameData createGame(String gameName) throws DataAccessException;
  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
  void addColor(GameData game, String playerColor) throws DataAccessException;
  void clear() throws DataAccessException;
}
