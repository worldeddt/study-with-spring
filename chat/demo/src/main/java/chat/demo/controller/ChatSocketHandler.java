package chat.demo.controller;


import chat.demo.controller.dto.ChatMessage;
import chat.demo.controller.dto.InviteMessage;
import chat.demo.enums.NotificationType;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.entity.SessionCache;
import chat.demo.sender.RedisPublisher;
import chat.demo.sender.StompNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@MessageMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatSocketHandler {
    private final StompNotificationSender stompNotificationSender;
    private final SessionCacheRepository sessionCacheRepository;
    private final RedisPublisher redisPublisher;

    @MessageMapping("/requestCall")
    public void call(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        final var principal = simpMessageHeaderAccessor.getUser();
        log.info("session : {}", simpMessageHeaderAccessor.getUser());

        final var roomId =
                "call_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                                +generateRandomChain(8);

        final var sessionCache = sessionCacheRepository.findByPrincipalName(principal.getName());


        Iterable<SessionCache> all = sessionCacheRepository.findAll();

        for (SessionCache cache : all) {
            stompNotificationSender.sendCallNotification(
                    cache.getPrincipalName(), ChatMessage.builder()
                                    .userId(sessionCache.getUserId())
                                    .message("hello :"+cache.getUserId())
                                    .type("MESSAGE")
                            .build());
        }

        if (sessionCache != null) {
            stompNotificationSender.sendCallNotification(sessionCache.getPrincipalName(),
                    InviteMessage.builder()
                            .type(NotificationType.OUTBOUND_CLIENT_CALL)
                            .sender(principal.getName()).build()
            );
        }
    }

    @MessageMapping("/session/all")
    public void sessionAll() {
        Iterable<SessionCache> all = sessionCacheRepository.findAll();
        all.forEach(sessionCache -> {
            log.debug("sessionCache : {}/ session id : {}", sessionCache.getPrincipalName(), sessionCache.getUserId());
        });
        sessionCacheRepository.deleteAll();
    }



    public static String generateRandomChain(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
