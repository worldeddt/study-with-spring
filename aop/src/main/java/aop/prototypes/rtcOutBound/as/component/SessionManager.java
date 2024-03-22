package aop.prototypes.rtcOutBound.as.component;


import aop.prototypes.rtcOutBound.as.model.SessionInfo;
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

    public SessionInfo findSessionInfo(String principalName) {
        return principalNameSessionInfo.get(principalName);
    }

    public SessionInfo findSessionInfo(Principal principal) {
        return findSessionInfo(principal.getName());
    }

}
