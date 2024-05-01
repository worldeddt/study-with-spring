package chat.demo.controller;


import chat.demo.controller.dto.InviteMessage;
import chat.demo.enums.NotificationType;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.domain.SessionCache;
import chat.demo.sender.StompNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@MessageMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatSocketHandler {
    private final StompNotificationSender stompNotificationSender;
    private final SessionCacheRepository sessionCacheRepository;

    @MessageMapping("/requestCall")
    public void call(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        final var principal = simpMessageHeaderAccessor.getUser();
        log.info("session : {}", simpMessageHeaderAccessor.getUser());

        final var roomId =
                "call_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                +generateRandomChain(8);


        Iterable<SessionCache> all = sessionCacheRepository.findAll();

        for (SessionCache sessionCache : all) {

            stompNotificationSender.sendCallNotification(sessionCache.getPrincipalName(),
                    InviteMessage.builder()
                            .notificationType(NotificationType.OUTBOUND_CLIENT_CALL)
                            .sender(principal.getName()).build()
            );
        }

    }


    public static String generateRandomChain(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
