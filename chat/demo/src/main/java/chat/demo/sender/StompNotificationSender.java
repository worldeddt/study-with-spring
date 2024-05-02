package chat.demo.sender;


import chat.demo.controller.dto.InviteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompNotificationSender {

    private static final String TOPIC_CALL_NOTIFICATION = "/queue/chat/callNotification";
    private static final String TOPIC_CALL_ERROR = "/queue/chat/error";
    private final SimpMessagingTemplate messageTemplate;

    public void sendCallNotification(String principalName,  InviteMessage inviteMessage) {
        log.info("sendCallNotification() - principalName: {}, object: {}", principalName, inviteMessage);
        messageTemplate.convertAndSendToUser(principalName, TOPIC_CALL_NOTIFICATION, inviteMessage);
    }
}
