package webchat.webrtc3phase.infra;


import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import webchat.webrtc3phase.domain.SessionIdOwner;
import webchat.webrtc3phase.dto.SocketSession;
import webchat.webrtc3phase.enums.RedisProperties;
import webchat.webrtc3phase.service.RedisService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UserRepository extends RedisService {
    private final ConcurrentHashMap<String, SocketSession> usersBySessionId
            = new ConcurrentHashMap<>();

    private static final String SOCKET_OWN =
            RedisProperties.PF_SOCK + RedisProperties.OWN + RedisProperties.COLON;

    public synchronized SessionIdOwner getSocketOwnerBySessionId(String sessionId) {
        String value = getStringByKey(SOCKET_OWN+sessionId);

        SessionIdOwner sessionIdOwner = null;

        if (value == null) return null;

        sessionIdOwner = (SessionIdOwner) JsonReader.jsonToJava(value);
        log.debug("레디스의 소켓오너 정보 가져옴 -> sessionId={}, subsId={}, userId={}",
                sessionId, sessionIdOwner.getSubsId(), sessionIdOwner.getUserId());
        return sessionIdOwner;
    }

    public synchronized void setSocketOwnerBySessionId(String id, SessionIdOwner sessionIdOwner) {
        updateSocketOwnerBySessionId(id, sessionIdOwner);
    }

    public synchronized void updateSocketOwnerBySessionId(String id, SessionIdOwner sessionIdOwner) {
        setStringByKey(SOCKET_OWN + id, JsonWriter.objectToJson(sessionIdOwner), 86400, TimeUnit.SECONDS);
    }

    public void listSocketSession() {
        log.info("listSocketSession...size:{}", usersBySessionId.mappingCount());
        for (final String sessionId : usersBySessionId.keySet()) {
            log.info("ConcurrentHashMap::usersBySessionId {}", sessionId);
        }
    }

    public void removeSocketOwnerBySessionId(String sessionId) {
        deleteKey(SOCKET_OWN + sessionId);
    }

    public SocketSession getBySessionId(String sessionId) {
        return usersBySessionId.get(sessionId);
    }

    public SocketSession removeBySessionId(String sessionId) {
        final SocketSession socketSession = getBySessionId(sessionId);
        // 210813
        //if (socketSession != null && socketSession.getUserId() != null) {
        if (socketSession != null) {
            usersBySessionId.remove(sessionId);

            listSocketSession();
        }

        return socketSession;
    }
}
