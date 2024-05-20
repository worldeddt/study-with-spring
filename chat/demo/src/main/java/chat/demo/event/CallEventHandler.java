package chat.demo.event;

import chat.demo.advice.CommonCode;
import chat.demo.advice.CommonException;
import chat.demo.application.SessionService;
import chat.demo.enums.UserStatus;
import chat.demo.model.SessionInfo;
import chat.demo.properties.ChatProperties;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.UserEntityRepository;
import chat.demo.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;


@RequiredArgsConstructor
@Component
@Slf4j
public class CallEventHandler {

    private final SessionService sessionService;
    private final SessionCacheRepository sessionCacheRepository;
    private final ChatProperties chatProperties;
    private final UserEntityRepository userEntityRepository;

    @EventListener(SessionDisconnectEvent.class)
    public synchronized void handleSessionDisconnectedListener(SessionDisconnectEvent event) {
        final var headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        final var principal = headerAccessor.getUser();
        sessionService.removeSession(principal);
    }

    @EventListener(SessionSubscribeEvent.class)
    public synchronized void handleSessionSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = headerAccessor.getUser();

        final var sessionInfo = sessionCacheRepository.findByPrincipalName(principal.getName());
        if (sessionInfo == null) {
            log.warn("");
            throw new CommonException(CommonCode.NOT_FOUND_SESSION);
        }

        final var userId = sessionInfo.getUserId();
        boolean isHost = sessionInfo.isHost();
        final var serverName = chatProperties.chatServer().serverName();
        log.info("[{}][{}][{}] sessionSubscribeEvent={}", null, userId, principal.getName(), event);

        if (isHost) {
            userEntityRepository.save(
                    User.builder()
                            .userId(userId)
                            .userStatus(UserStatus.RG)
                            .callId(null)
                            .server(serverName)
                            .build());
        }
    }
}
