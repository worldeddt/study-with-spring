package media.ftf.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class SessionManager {
    private final ConcurrentHashMap<String, SessionInfo> sessionsMap
            = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, String> userIdMap
            = new ConcurrentHashMap<>();

    public void registerSession(SessionInfo sessionInfo) {
        final var user = sessionInfo.getPrincipal();
        final var userId = sessionInfo.getUserId();
        sessionsMap.putIfAbsent(user.getName(), sessionInfo);
        userIdMap.computeIfAbsent(userId, k -> user.getName());
    }

    public synchronized SessionInfo removeSession(Principal user) {
        String principalName = user.getName();
        SessionInfo sessionInfo = sessionsMap.remove(principalName);
        userIdMap.remove(sessionInfo.getUserId());
        return sessionInfo;
    }

    public boolean hasSession(SessionInfo sessionInfo) {
        return sessionsMap.containsKey(sessionInfo.getPrincipal().getName());
    }

    public SessionInfo find() {

    }
}
