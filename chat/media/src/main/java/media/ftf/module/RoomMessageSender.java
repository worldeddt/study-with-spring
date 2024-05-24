package media.ftf.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.application.dto.response.EndRoomMessage;
import media.ftf.dto.response.MultiMediaNotificatonMessage;
import media.ftf.enums.RoomNotificationType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Slf4j
@Component
@RequiredArgsConstructor
public class RoomMessageSender  {

    private static final String TOPIC_MULTIMEDIA_NOTIFICATION = "/queue/room/multiMediaNotification";

    private final SimpMessagingTemplate messagingTemplate;

    public void sendEndRoomMessage(Principal principal, EndRoomMessage endRoomMessage) {
        log.info("{}, {}", principal, endRoomMessage);
        messagingTemplate.convertAndSendToUser(principal.getName(),
                TOPIC_MULTIMEDIA_NOTIFICATION, MultiMediaNotificatonMessage.builder()
                .type(RoomNotificationType.END_ROOM)
                .payload(endRoomMessage)
                .build());
    }
}
