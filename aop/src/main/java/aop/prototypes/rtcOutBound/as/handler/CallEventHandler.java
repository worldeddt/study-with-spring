package aop.prototypes.rtcOutBound.as.handler;

import aop.prototypes.rtcOutBound.as.component.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallEventHandler {

    private final SessionManager sessionManager;
    @EventListener(SessionSubscribeEvent.class)
    public void handleSessionSubscribeListener(SessionSubscribeEvent event) {
        final var headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final var principal = headerAccessor.getUser();

        log.info("event headerAccessor : {}", headerAccessor.getDestination());
        if ("/user/queue/call/callNotification".equals(headerAccessor.getDestination())) {
            final var sessionInfo = sessionManager.findSessionInfo(principal);
            final var userId = sessionInfo.getUserId();
            final var isAgent = sessionInfo.isAgent();

//            if (Boolean.TRUE.equals(isAgent))
//                agentStatusManager.registerAgent(userId);
        }
    }
}
