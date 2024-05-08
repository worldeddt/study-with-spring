package chat.demo.event;

import chat.demo.application.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@RequiredArgsConstructor
public class CallEventHandler {

    private final SessionService sessionService;

    @EventListener(SessionDisconnectEvent.class)
    public synchronized void handleSessionDisconnectedListener(SessionDisconnectEvent event) {
        final var headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final var principal = headerAccessor.getUser();
        sessionService.removeSession(principal);
    }
}
