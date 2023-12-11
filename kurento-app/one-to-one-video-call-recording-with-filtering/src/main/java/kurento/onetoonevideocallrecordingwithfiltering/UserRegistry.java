package kurento.onetoonevideocallrecordingwithfiltering;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class UserRegistry {


    private ConcurrentHashMap<String, UserSession> usersByName
            = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, UserSession> usersBySessionId = new ConcurrentHashMap<>();

    public void register(UserSession userSession) {
        usersByName.put(userSession.getName(), userSession);
        usersBySessionId.put(userSession.getSessionId(), userSession);
    }

    public UserSession getByName(String name) {
        return usersByName.get(name);
    }

    public UserSession getBySession(WebSocketSession session) {
        return usersBySessionId.get(session.getId());
    }

    public boolean exists(String name) {
        return usersByName.keySet().contains(name);
    }
    public UserSession removeBySession(WebSocketSession session) {
        final UserSession user = getBySession(session);
        if (user != null) {
            usersByName.remove(user.getName());
            usersBySessionId.remove(session.getId());
        }
        return user;
    }
}
