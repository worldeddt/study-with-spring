package web.coviewpractice.components;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.coviewpractice.components.redis.InviteManager;
import web.coviewpractice.dto.SessionInfo;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class SessionManager {
    private final Map<String, SessionInfo> principalNameSessionInfo = new ConcurrentHashMap<>();
    private final Map<String, String> userIdIndex = new ConcurrentHashMap<>();
    private final InviteManager inviteManager;

    public void registerSession(SessionInfo sessionInfo) {
        final var principal = sessionInfo.getPrincipal();
        final var principalName = principal.getName();
        final var userId = sessionInfo.getUserId();
        principalNameSessionInfo.putIfAbsent(principalName, sessionInfo);
        userIdIndex.computeIfAbsent(userId, k -> principalName);
        if(sessionInfo.isAgent()) inviteManager.deleteAllInviteKeyByUserId(userId);
    }

    public void removeSession(Principal principal) {
        final var name = principal.getName();
        final var sessionInfo = principalNameSessionInfo.remove(name);
        final var userId = sessionInfo.getUserId();
        userIdIndex.remove(userId);
        if(sessionInfo.isAgent()) inviteManager.deleteAllInviteKeyByUserId(userId);
    }

    public boolean hasSession(SessionInfo sessionInfo) {
        final var userId = sessionInfo.getUserId();
        return userIdIndex.containsKey(userId);
    }

    public Principal findPrincipal(String userId) {
        final var principalName = userIdIndex.get(userId);
        final var sessionInfo = findSessionInfo(principalName);
        return sessionInfo.getPrincipal();
    }

    public SessionInfo findSessionInfo(String principalName) {
        return principalNameSessionInfo.get(principalName);
    }

    public SessionInfo findSessionInfo(Principal principal) {
        return findSessionInfo(principal.getName());
    }

}
