package client;

import com.google.gson.Gson;
import responseexception.ResponseException;
import model.AuthData;
import model.GameData;
import request.*;
import result.*;
import http.HttpCommunicator;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;
import websocket.commands.UserGameCommand;

import java.util.Collection;

public class ServerFacade {
  private final HttpCommunicator httpCommunicator;
  private final WebSocketCommunicator webSocketCommunicator;
  private final ServerMessageObserver observer;
  private final Gson gson = new Gson();

  public ServerFacade(String serverUrl, ServerMessageObserver observer) throws Exception {
    this.httpCommunicator=new HttpCommunicator(serverUrl);
    this.webSocketCommunicator=new WebSocketCommunicator(serverUrl, observer);
    this.observer=observer;
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
  public void joinGame(String authToken, Integer gameID, String color) throws ResponseException {
    try {
      UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
      String message = new Gson().toJson(command);
      webSocketCommunicator.send(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void testWebSocket() throws Exception {
    webSocketCommunicator.send(gson.toJson("Test message"));
  }
}