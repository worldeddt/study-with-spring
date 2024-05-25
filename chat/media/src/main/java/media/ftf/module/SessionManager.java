package media.ftf.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class SessionManager {

    private final Map<String, SessionInfo> principalNameSessionInfo = new ConcurrentHashMap<>();
    private final Map<String, String> userIdIndex = new ConcurrentHashMap<>();

    public synchronized void registerSession(SessionInfo sessionInfo) {
        final var user = sessionInfo.getPrincipal();
        final var name = user.getName();
        final var userId = sessionInfo.getUserId();
        principalNameSessionInfo.putIfAbsent(name, sessionInfo);
        userIdIndex.computeIfAbsent(userId, k -> name);
    }

    public synchronized SessionInfo removeSession(Principal principal) {
        final var principalName = principal.getName();
        final var sessionInfo = principalNameSessionInfo.remove(principalName);
        final var userId = sessionInfo.getUserId();
        userIdIndex.remove(userId);
        return sessionInfo;
    }

    public Principal findPrincipalByUserId(String userId) {
        if (userId == null) return null;
        final var principalName = userIdIndex.get(userId);
        if (principalName == null) return null;
        final var sessionInfo = findSessionInfoByPrincipalName(principalName);
        return sessionInfo.getPrincipal();
    }

    public SessionInfo findSessionInfoByUserId(String userId) {
        final var principalName = userIdIndex.get(userId);
        return principalNameSessionInfo.get(principalName);
    }

    public SessionInfo findSessionInfoByPrincipalName(String principalName) {
        return principalNameSessionInfo.get(principalName);
    }

    public SessionInfo findSessionInfo(Principal principal) {
        return findSessionInfoByPrincipalName(principal.getName());
    }

    public boolean hasSession(SessionInfo sessionInfo) {
        final var userId = sessionInfo.getUserId();
        return userIdIndex.containsKey(userId);
    }

}