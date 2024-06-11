package client;

import responseexception.ResponseException;
import model.AuthData;
import model.GameData;
import request.*;
import result.*;
import http.HttpCommunicator;
import websocket.WebSocketCommunicator;

import java.util.Collection;

public class ServerFacade {
  private final HttpCommunicator httpCommunicator;
  private final WebSocketCommunicator webSocketCommunicator;

  public ServerFacade(String serverUrl) throws Exception {
    this.httpCommunicator=new HttpCommunicator(serverUrl);
    this.webSocketCommunicator=new WebSocketCommunicator(serverUrl);
  }

  public AuthData register(String username, String password, String email) throws ResponseException {
    RegisterRequest request=new RegisterRequest(username, password, email);
    RegisterResult result=this.httpCommunicator.makeRequest("POST", "/user", request, RegisterResult.class, null);

    if (result.message() != null && result.message().contains("error")) {
      throw new ResponseException(400, result.message());
    }

    return new AuthData(result.authToken(), result.username());
  }

  public AuthData login(String username, String password) throws ResponseException {
    LoginRequest request=new LoginRequest(username, password);
    LoginResult result=this.httpCommunicator.makeRequest("POST", "/session", request, LoginResult.class, null);

    if (result.message() != null && result.message().contains("error")) {
      throw new ResponseException(400, result.message());
    }

    return new AuthData(result.authToken(), result.username());
  }

  public AuthData logout(AuthData authData) throws ResponseException {
    LogoutRequest request=new LogoutRequest(authData.authToken());
    LogoutResult result=this.httpCommunicator.makeRequest("DELETE", "/session", request, LogoutResult.class, authData.authToken());

    return new AuthData(null, result.message());
  }

  public GameData create(String authToken, String gameName) throws ResponseException {
    CreateGameRequest request=new CreateGameRequest(authToken, gameName);
    CreateGameResult result=this.httpCommunicator.makeRequest("POST", "/game", request, CreateGameResult.class, authToken);

    if (result.message() != null && result.message().contains("error")) {
      throw new ResponseException(400, result.message());
    }

    return new GameData(result.gameID(), null, null, gameName, result.game());
  }


  public Collection<GameData> list(String authToken) throws ResponseException {
    ListGamesRequest request=new ListGamesRequest(authToken);
    ListGamesResult result=this.httpCommunicator.makeRequest("GET", "/game", request, ListGamesResult.class, authToken);

    return result.games();
  }

  public GameData join(String authToken, Integer gameID, String color) throws ResponseException {
    JoinGameRequest request=new JoinGameRequest(authToken, color, gameID);
    JoinGameResult result=this.httpCommunicator.makeRequest("PUT", "/game", request, JoinGameResult.class, authToken);


    return new GameData(gameID, result.whiteUsername(), result.blackUsername(), result.gameName(), result.game());
  }
}