package dataaccess.gamedaos;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import java.util.Collection;

public interface GameDao {
  Integer createGameID(GameData game) throws DataAccessException;
  GameData createGame(String gameName) throws DataAccessException;
  GameData getGame(Integer gameID) throws DataAccessException;
  Collection<GameData> listGames() throws DataAccessException;
  String getPlayerColor(Integer gameID, String username) throws DataAccessException;
  void addColor(Integer gameID, String playerColor, String username) throws DataAccessException;
  void removeColor(Integer gameID, String playerColor) throws DataAccessException;
  void updateGame(Integer gameID, ChessGame game) throws DataAccessException;
  void clear();
}
