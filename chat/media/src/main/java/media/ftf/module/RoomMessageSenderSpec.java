package media.ftf.module;

import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import media.ftf.enums.MemberNotificationType;
import media.ftf.handler.dto.*;

import java.security.Principal;

public interface RoomMessageSenderSpec {

    String QUEUE_ANSWER = "/queue/room/answer";
    String QUEUE_CANDIDATE = "/queue/room/candidate";
    String QUEUE_MULTIMEDIA_NOTIFICATION = "/queue/room/multiMediaNotification";
    String QUEUE_DATA = "/queue/room/bypass";
    String TOPIC_DATA = "/topic/room/bypass";
    String QUEUE_CHAT = "/queue/room/chat";
    String TOPIC_CHAT = "/topic/room/chat";
    String QUEUE_ERROR = "/queue/room/error";

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-answer",
                    payloadType = AnswerMessage.class
            )
    )
    void sendAnswer(Principal principal, AnswerMessage answerMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-candidate",
                    payloadType = CandidateMessage.class
            )
    )
    void sendCandidate(Principal principal, CandidateMessage candidateMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-multiMediaNotification (mode=member)",
                    payloadType = MultiMediaNotificationMessage.MemberResponseMessage.class
            )
    )
    void sendMemberMessage(Principal principal, SessionInfo enterSessionInfo,
                           MemberNotificationType memberNotificationType);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-multiMediaNotification (mode=record)",
                    payloadType = MultiMediaNotificationMessage.RecordResponseMessage.class
            )
    )
    void sendRecordMessage(Principal principal, RecordControlDto recordControlDto);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-multiMediaNotification (mode=recordCheck)",
                    payloadType = MultiMediaNotificationMessage.RecordCheckResponseMessage.class
            )
    )
    void sendRecordCheckMessage(Principal principal, RecordCheckDto recordCheckDto);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-multiMediaNotification (mode=endRoom)",
                    payloadType = MultiMediaNotificationMessage.EndRoomResponseMessage.class
            )
    )
    void sendEndRoomMessage(Principal principal, EndRoomDto endRoomMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "topic-room-chat",
                    payloadType = ChatMessage.class
            )
    )
    void sendChat(String roomId, ChatMessage chatMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-chat",
                    payloadType = ChatMessage.class
            )
    )
    void sendChat(Principal principal, ChatMessage chatMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "topic-room-data",
                    payloadType = DataMessage.class
            )
    )
    void sendData(String roomId, DataMessage dataMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-queue-room-data",
                    payloadType = DataMessage.class
            )
    )
    void sendData(Principal principal, DataMessage dataMessage);

    @AsyncPublisher(
            operation = @AsyncOperation(
                    channelName = "user-room-error",
                    payloadType = ErrorMessage.class
            )
    )
    void sendError(Principal principal, ErrorMessage errorMessage);
}