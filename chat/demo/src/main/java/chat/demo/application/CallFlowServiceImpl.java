package chat.demo.application;

import chat.demo.advice.CommonCode;
import chat.demo.advice.CommonException;
import chat.demo.application.dto.AcceptCallMessage;
import chat.demo.application.dto.RefuseInviteMessage;
import chat.demo.application.dto.RequestCallMessage;
import chat.demo.application.interfaces.CallFlowService;
import chat.demo.component.CallManager;
import chat.demo.component.InviteManager;
import chat.demo.component.MultimediaClient;
import chat.demo.controller.dto.InviteMessage;
import chat.demo.controller.dto.TicketMessage;
import chat.demo.controller.dto.request.RoomRequest;
import chat.demo.controller.dto.response.ClientResponse;
import chat.demo.controller.dto.response.CreateRoomResponse;
import chat.demo.controller.dto.response.Ticket;
import chat.demo.enums.*;
import chat.demo.repository.*;
import chat.demo.repository.entity.Call;
import chat.demo.repository.entity.Involvement;
import chat.demo.repository.entity.Participant;
import chat.demo.sender.RedisPublisher;
import chat.demo.sender.StompNotificationSender;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PrinterInfo;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CallFlowServiceImpl implements CallFlowService {

    private final InviteManager inviteManager;
    private final SessionCacheRepository sessionCacheRepository;
    private final UserEntityRepository userEntityRepository;
    private final InvolvementEntityRepository involvementEntityRepository;
    private final ParticipantEntityRepository participantEntityRepository;
    private final StompNotificationSender stompNotificationSender;
    private final RedisPublisher redisPublisher;
    private final MultimediaClient multimediaClient;
    private final CallEntityRepository callEntityRepository;
    private final CallManager callManager;

    @Override
    public void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage) {
        log.info("session number : {}", principal.getName());

        final var sessionCache = sessionCacheRepository.findByPrincipalName(principal.getName());

        if (sessionCache == null) {
            throw new CommonException(CommonCode.NOT_FOUND_SESSION);
        }

        final var userId = sessionCache.getUserId();

        final var inviteKey = acceptCallMessage.getInviteKey();

        log.info("invite key : {}", inviteKey);

        final var inviteKeyEntity = inviteManager.useInviteKey(inviteKey)
                .orElseThrow(() -> new CommonException(CommonCode.UNKNOWN_INVITE_KEY));

        if (StringUtils.equals(inviteKeyEntity.getUserId(), userId)) {
            log.warn("");
            throw new CommonException(CommonCode.NOT_INVITE_YOURSELF);
        }

        handleOutboundAcceptCall(principal, acceptCallMessage);
    }

    @Override
    public void handleRequestCall(Principal principal, RequestCallMessage requestCallMessage) {

        final var sessionInfo = sessionCacheRepository.findByPrincipalName(principal.getName());

        final var roomId =
                "call-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + "-" + generateRandomChain(8);

        final var newCall = Call.builder()
                .id(roomId)
                .callType(CallType.OUTBOUND_CLIENT)
                .caller(sessionInfo.getUserId())
                .owner(sessionInfo.getUserId())
                .closeReason(CallClosedReason.Uncompleted)
                .build();

        final var savedCall = callEntityRepository.save(newCall);

        final var newParticipant = Participant.builder()
                .call(savedCall)
                .userId(sessionInfo.getUserId())
                .userType(EUserType.HOST)
                .loginId(sessionInfo.getLoginId())
                .userName(sessionInfo.getUserName())
                .build();

        final var savedParticipant = participantEntityRepository.save(newParticipant);

        final var roomRequest =
                RoomRequest.builder().roomId(roomId).userId(sessionInfo.getUserId()).build();

        ClientResponse<CreateRoomResponse> clientResponse = multimediaClient.createRoom(roomRequest);

        if (clientResponse.getHeaderStatusCode().isError()) {
            log.info("response isError: {}", clientResponse.getHeaderStatusCode().isError());
            throw new CommonException(CommonCode.MAKE_TICKET_FAILED);
        }

        if (!clientResponse.getHeaderStatusCode().is2xxSuccessful()) {
            log.info("response is2xxSuccessful: {}", clientResponse.getHeaderStatusCode().is2xxSuccessful());
            throw new CommonException(CommonCode.NOT_SUCCESS_REST_RESP);
        }

        final var body = clientResponse.getBodyStatusCode();

        if (body == null) {
            log.info("body is null");
            throw new CommonException(CommonCode.NULL_BODY);
        }

        if (200 != body) {
            log.info("body statusCode: {}", body);
            throw new CommonException(CommonCode.NOT_SUCCESS_RESP);
        }

        final var createRoomResponse = clientResponse.getResponse();

        final var mediaServer = createRoomResponse.getMediaServer();
        final var multiMediaServer = createRoomResponse.getMultiMediaServer();

        Call call = savedCall.toBuilder()
                .multiMediaServer(multiMediaServer)
                .mediaServer(mediaServer)
                .build();

        stompNotificationSender.sendCallNotification(
                principal.getName(),
                InviteMessage.builder()
                        .sender(sessionInfo.getUserId())
                        .type(NotificationType.OUTBOUND_CLIENT_CALL)
                        .inviteKey(
                                inviteManager.createInviteKey(
                                        sessionInfo.getUserId(),
                                        CallType.OUTBOUND_CLIENT,
                                        null, call
                                )
                        )
                        .build()
        );
    }


    @Transactional
    public void handleOutboundAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage) {
        final var sessionInfo = sessionCacheRepository.findByPrincipalName(principal.getName());
        if (sessionInfo == null) {
            log.warn("");
            throw new CommonException(CommonCode.NOT_FOUND_SESSION);
        }

        final var userId = sessionInfo.getUserId();
        final var inviteKey = acceptCallMessage.getInviteKey();
        final var isAccept = acceptCallMessage.isAccept();

        final var inviteKeyEntity = inviteManager.useInviteKey(inviteKey)
                .orElseThrow(() -> new CommonException(CommonCode.UNKNOWN_INVITE_KEY));

        final var hostId = inviteKeyEntity.getUserId();

        log.info("host Id : {}", hostId);
        final var hostUser = userEntityRepository.findById(hostId)
                .orElseThrow(() -> new CommonException(CommonCode.NOT_FOUND_AGENT));

        final var callId = hostUser.getCallId();

        if (callId == null)
            throw new CommonException(CommonCode.NOT_FOUND_CALL_ID);

        final var guestUser = userEntityRepository.findById(userId)
                .orElseThrow(() -> new CommonException(CommonCode.NOT_FOUND_USER));

        if (!isAccept) {
            redisPublisher.publishByUserId(hostId,
                    RefuseInviteMessage.builder()
                    .userId(userId)
                    .inviteKey(inviteKey)
                    .build()
            );
        } else {
            callManager.put(callId, userId);

            Call call = callEntityRepository.findById(callId).get();
            call.setStartDate(LocalDateTime.now());

            //방장 등록
            involvementEntityRepository.save(
                    Involvement.builder()
                            .call(call)
                            .agentId(hostId)
                            .type(InvolvementType.HANDOVER)
                            .build()
            );

            final var mediaServer = call.getMediaServer();
            final var multiMediaServer = call.getMultiMediaServer();

            final var newParticipant = Participant.builder()
                    .call(call)
                    .userId(userId)
                    .userName(guestUser.getUsername())
                    .userType(EUserType.CLIENT)
                    .build();

            participantEntityRepository.save(newParticipant);

            stompNotificationSender.sendCallNotification(principal.getName(),
                    TicketMessage.builder()
                            .ticket(
                                    Ticket.builder()
                                            .multiMediaServer(multiMediaServer)
                                            .mediaServer(mediaServer)
                                            .roomId(callId)
                                            .build()
                            )
                            .build()
                    );
        }
    }

    public static String generateRandomChain(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
