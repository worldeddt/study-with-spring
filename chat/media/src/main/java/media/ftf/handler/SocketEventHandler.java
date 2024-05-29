package media.ftf.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.application.interfaces.SocketService;
import media.ftf.application.interfaces.SocketSessionService;
import media.ftf.utils.IpUtils;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class SocketEventHandler {

    private final SocketService socketService;
    private final SocketSessionService socketSessionServiceImpl;

    @EventListener(SessionConnectedEvent.class)
    public synchronized void handleSessionConnectedListener(SessionConnectedEvent event) {
        log.info("{}", event.getUser());
        final var headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final var user = headerAccessor.getUser();
        final var connectMessageHeader = headerAccessor.getMessageHeaders().get(
                SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER, Message.class);
        final var connectHeaderAccessor = StompHeaderAccessor.wrap(connectMessageHeader);
        final var userId = connectHeaderAccessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        final var roomId = connectHeaderAccessor.getFirstNativeHeader("ROOM");

        if (connectHeaderAccessor.getSessionAttributes() != null) {
            log.info("{}", IpUtils.getIpInfo(connectHeaderAccessor.getSessionAttributes()));
        }

        socketSessionServiceImpl.unregister(user, socketService::handleLeave);

        socketSessionServiceImpl.register(user, userId, roomId);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleSessionDisconnectedListener(SessionDisconnectEvent event) {
        log.info("{}", event.getUser());
        socketSessionServiceImpl.unregister(event.getUser(), socketService::handleLeave);
    }

}