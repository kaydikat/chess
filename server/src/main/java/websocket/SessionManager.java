package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class SessionManager {
  private final ConcurrentHashMap<Integer, Set<Session>> gameSessions = new ConcurrentHashMap<>();
  private final Gson gson = new Gson();

  public void add(Integer gameID, Session session) {
    Set<Session> sessions = gameSessions.get(gameID);
    if (sessions == null) {
      sessions = new CopyOnWriteArraySet<>();
      gameSessions.put(gameID, sessions);
    }
    sessions.add(session);
  }

  public void remove(Integer gameID, Session session) {
    Set<Session> sessions = gameSessions.get(gameID);
    if (sessions != null) {
      sessions.remove(session);
      if (sessions.isEmpty()) {
        gameSessions.remove(gameID);
      }
    }
  }

  public void broadcast(Integer gameID, ServerMessage message) throws IOException {
    Set<Session> sessions = gameSessions.get(gameID);
    if (sessions != null) {
      for (Session session : sessions) {
        if (session.isOpen()) {
          session.getRemote().sendString(gson.toJson(message));
        }
      }
    }
  }
}
