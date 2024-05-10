package chat.demo.controller;


import chat.demo.advice.CommonCode;
import chat.demo.advice.CommonException;
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
    private final MultimediaClient multimediaClient;
    private final CallEntityRepository callEntityRepository;
    private final InviteManager inviteManager;


    @Transactional(rollbackOn = Exception.class)
    @MessageMapping("/requestCall")
    public void call(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        final var principal = simpMessageHeaderAccessor.getUser();
        log.info("session : {}", simpMessageHeaderAccessor.getUser());

        final var sessionCache = sessionCacheRepository.findByPrincipalName(principal.getName());

        final var roomId =
                "call-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + "-" + generateRandomChain(8);

        final var newCall = Call.builder()
                .id(roomId)
                .callType(CallType.OUTBOUND_CLIENT)
                .caller(sessionCache.getUserId())
                .owner(sessionCache.getUserId())
                .closeReason(CallClosedReason.Uncompleted)
                .build();

        final var savedCall = callEntityRepository.save(newCall);
        
        final var roomRequest =
                RoomRequest.builder().roomId(roomId).userId(sessionCache.getUserId()).build();

        ClientResponse<CreateRoomResponse> clientResponse = multimediaClient.createRoom(roomRequest);

        if (clientResponse.getHeaderStatusCode().isError()) {
            log.debug("response isError: {}", clientResponse.getHeaderStatusCode().isError());
            throw new CommonException(CommonCode.MAKE_TICKET_FAILED);
        }

        if (!clientResponse.getHeaderStatusCode().is2xxSuccessful()) {
            log.debug("response is2xxSuccessful: {}", clientResponse.getHeaderStatusCode().is2xxSuccessful());
            throw new CommonException(CommonCode.NOT_SUCCESS_REST_RESP);
        }

        final var body = clientResponse.getBodyStatusCode();

        if (body == null) {
            log.debug("body is null");
            throw new CommonException(CommonCode.NULL_BODY);
        }

        if (200 != body) {
            log.debug("body statusCode: {}", body);
            throw new CommonException(CommonCode.NOT_SUCCESS_RESP);
        }

        final var createRoomResponse = clientResponse.getResponse();

        final var mediaServer = createRoomResponse.getMediaServer();
        final var multiMediaServer = createRoomResponse.getMultiMediaServer();

        Call buildedCall = savedCall.toBuilder()
                .multiMediaServer(multiMediaServer)
                .mediaServer(mediaServer)
                .build();

        stompNotificationSender.sendCallNotification(
                principal.getName(),
                InviteMessage.builder()
                        .sender(sessionCache.getUserId())
                        .type(NotificationType.OUTBOUND_CLIENT_CALL)
                        .inviteKey(
                                inviteManager.createInviteKey(
                                        sessionCache.getUserId(),
                                        CallType.OUTBOUND_CLIENT,
                                         null, buildedCall
                                )
                        )
                        .build()
        );
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
