package dataaccess;

import model.GameData;
import java.util.Collection;

public interface GameDao {
  void createGame(GameData game) throws DataAccessException;
  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
  void updateGame(GameData game) throws DataAccessException;
  void addColor(GameData game, String playerColor) throws DataAccessException;
  void clear() throws DataAccessException;
}
