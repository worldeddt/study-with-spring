package webchat.webrtc3phase.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import webchat.webrtc3phase.dto.SessionInfo;
import webchat.webrtc3phase.service.SessionService;
import webchat.webrtc3phase.utils.FermiConnectMessageParser;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SessionService sessionService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectEvent) {
        log.info("Received a new web socket connection handle for connect web socket");
//        log.info("message 1= {}", sessionConnectedEvent.getMessage());
        log.info("message 2= {}", sessionConnectEvent.getMessage());


        SessionInfo si = new SessionInfo();
//        si.setSessionId(sessionId);
//        si.setUserId(userId);
    }
}
