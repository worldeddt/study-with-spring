package media.ftf.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.enums.MemberNotificationType;
import media.ftf.enums.RoomNotificationType;
import media.ftf.handler.dto.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Slf4j
@RequiredArgsConstructor
@Component
public class RoomMessageSender implements RoomMessageSenderSpec {

    private final SimpMessagingTemplate messageTemplate;

    public void sendAnswer(Principal principal, AnswerMessage answerMessage) {
        log.debug("sendAnswer() - principal: {}, obj: {}", principal, answerMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_ANSWER, answerMessage);
    }

    public void sendCandidate(Principal principal, CandidateMessage candidateMessage) {
        log.debug("sendCandidate() - principal: {}, obj: {}", principal, candidateMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_CANDIDATE, candidateMessage);
    }

    public void sendMemberMessage(Principal principal, SessionInfo enterSessionInfo, MemberNotificationType memberNotificationType) {
        log.debug("sendMemberEnterMessage() - principal: {}, SessionInfo: {} memberNotificationType: {}", principal, enterSessionInfo, memberNotificationType);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_MULTIMEDIA_NOTIFICATION, MultiMediaNotificationMessage.builder()
                .type(RoomNotificationType.MEMBER)
                .payload(MemberDto.builder()
                        .state(memberNotificationType)
                        .roomId(enterSessionInfo.getRoomId())
                        .userId(enterSessionInfo.getUserId())
                        .userType(enterSessionInfo.getUserType())
                        .userName(enterSessionInfo.getUserName())
                        .tenantId(enterSessionInfo.getTenantId())
                        .loginId(enterSessionInfo.getLoginId())
                        .deviceInfo(enterSessionInfo.getDeviceInfo())
                        .options(enterSessionInfo.getOptions()).build())
                .build());
    }

    public void sendRecordMessage(Principal principal, RecordControlDto recordControlDto) {
        log.debug("sendRecordMessage() - principal: {}, obj: {}", principal, recordControlDto);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_MULTIMEDIA_NOTIFICATION, MultiMediaNotificationMessage.builder()
                .type(RoomNotificationType.RECORD)
                .payload(recordControlDto)
                .build());
    }

    public void sendRecordCheckMessage(Principal principal, RecordCheckDto recordCheckDto) {
        log.debug("sendRecordCheckMessage() - principal: {}, obj: {}", principal, recordCheckDto);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_MULTIMEDIA_NOTIFICATION, MultiMediaNotificationMessage.builder()
                .type(RoomNotificationType.RECORD_CHECK)
                .payload(recordCheckDto)
                .build());
    }

    public void sendEndRoomMessage(Principal principal, EndRoomDto endRoomMessage) {
        log.debug("{}, {}", principal, endRoomMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_MULTIMEDIA_NOTIFICATION, MultiMediaNotificationMessage.builder()
                .type(RoomNotificationType.END_ROOM)
                .payload(endRoomMessage)
                .build());
    }

    public void sendChat(String roomId, ChatMessage chatMessage) {
        log.debug("sendChat() - dataMessage: {}", chatMessage);
        messageTemplate.convertAndSend(TOPIC_CHAT + "/" + roomId, chatMessage);
    }

    public void sendChat(Principal principal, ChatMessage chatMessage) {
        log.debug("sendChat() - principal: {}, dataMessage: {}", principal, chatMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_CHAT, chatMessage);
    }

    public void sendData(String roomId, DataMessage dataMessage) {
        log.debug("sendData() - dataMessage: {}", dataMessage);
        messageTemplate.convertAndSend(TOPIC_DATA + "/" + roomId, dataMessage);
    }

    public void sendData(Principal principal, DataMessage dataMessage) {
        log.debug("sendData() - principal: {}, dataMessage: {}", principal, dataMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_DATA, dataMessage);
    }

    public void sendError(Principal principal, ErrorMessage errorMessage) {
        log.debug("sendError() - principal: {}, obj: {}", principal, errorMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), QUEUE_ERROR, errorMessage);
    }
}