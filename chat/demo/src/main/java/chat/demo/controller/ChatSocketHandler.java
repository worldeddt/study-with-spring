package chat.demo.controller;


import chat.demo.advice.CommonCode;
import chat.demo.advice.CommonException;
import chat.demo.application.dto.AcceptCallMessage;
import chat.demo.application.dto.RequestCallMessage;
import chat.demo.application.interfaces.CallFlowService;
import chat.demo.component.InviteManager;
import chat.demo.component.MultimediaClient;
import chat.demo.controller.dto.ChatMessage;
import chat.demo.controller.dto.InviteMessage;
import chat.demo.controller.dto.request.RoomRequest;
import chat.demo.controller.dto.response.ClientResponse;
import chat.demo.controller.dto.response.CreateRoomResponse;
import chat.demo.enums.CallClosedReason;
import chat.demo.enums.CallType;
import chat.demo.enums.NotificationType;
import chat.demo.repository.CallEntityRepository;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.entity.Call;
import chat.demo.repository.entity.SessionCache;
import chat.demo.sender.RedisPublisher;
import chat.demo.sender.StompNotificationSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@MessageMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatSocketHandler {
    private final SessionCacheRepository sessionCacheRepository;
    private final CallFlowService callFlowService;

    @MessageMapping("/acceptCall")
    public void acceptCall(SimpMessageHeaderAccessor simpMessageHeaderAccessor,
                           AcceptCallMessage acceptCallMessage) {
        Principal user = simpMessageHeaderAccessor.getUser();
        SessionCache byPrincipalName = sessionCacheRepository.findByPrincipalName(user.getName());
        log.info("byPrincipalName : {}", byPrincipalName);
        callFlowService.handleAcceptCall(simpMessageHeaderAccessor.getUser(), acceptCallMessage);
    }

    @Transactional(rollbackOn = Exception.class)
    @MessageMapping("/requestCall")
    public void call(SimpMessageHeaderAccessor simpMessageHeaderAccessor,
                     RequestCallMessage requestCallMessage
                     ) {
        final var principal = simpMessageHeaderAccessor.getUser();
        log.info("session : {}", simpMessageHeaderAccessor.getUser());

        callFlowService.handleRequestCall(principal, requestCallMessage);
    }

    @MessageMapping("/session/all")
    public void sessionAll() {
        Iterable<SessionCache> all = sessionCacheRepository.findAll();
        all.forEach(sessionCache -> {
            log.info("sessionCache : {}/ session id : {}", sessionCache.getPrincipalName(), sessionCache.getUserId());
        });
        sessionCacheRepository.deleteAll();
    }
}
