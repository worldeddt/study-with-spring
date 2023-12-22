package webchat.webrtc3phase.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import webchat.webrtc3phase.domain.SessionIdOwner;
import webchat.webrtc3phase.dto.SessionInfo;
import webchat.webrtc3phase.enums.EUserType;
import webchat.webrtc3phase.infra.UserRepository;
import webchat.webrtc3phase.service.SessionService;
import webchat.webrtc3phase.utils.FermiConnectMessageParser;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SessionService sessionService;
    private final UserRepository userRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent sessionConnectEvent) {
//        log.info("message 1= {}", sessionConnectedEvent.getMessage());
        log.info("message 2= {}", sessionConnectEvent.getMessage());

        FermiConnectMessageParser fermiConnectMessageParser
                = FermiConnectMessageParser.wrap(sessionConnectEvent.getMessage());

        if (fermiConnectMessageParser == null) return;

        String sessionId = fermiConnectMessageParser.getSessionId();
        String subsId = fermiConnectMessageParser.getSubsId();
        String userType = fermiConnectMessageParser.getUserType();
        String userId = fermiConnectMessageParser.getUserId();

        if (sessionId != null && subsId != null && userType != null) {
            SessionInfo oldSessionInfo = sessionService.findSessionByUserId(userId);

            if(EUserType.CLIENT.name().equals(userType) && oldSessionInfo != null) {
                String oldSessionId = oldSessionInfo.getSessionId();
                log.debug("고객이 같은 아이디의 세션이 남아있을 경우 기존 세션이 접속해있는 방을 나가고 세션리소스 정리" +
                        " -> reconnectClientId={}, oldSessionId={} newSessionId={}", userId, oldSessionId, sessionId);

                SessionIdOwner oldSio = userRepository.getSocketOwnerBySessionId(oldSessionId);
            }

            SessionInfo si = new SessionInfo();
            si.setSessionId(sessionId);
            si.setUserId(userId);
            si.setSubsId(subsId);
            si.setUserType(EUserType.valueOf(userType));

            sessionService.putSessionByUserId(userId, si);

            userRepository.setSocketOwnerBySessionId(sessionId, new SessionIdOwner(subsId, userId));
        }
    }
}
