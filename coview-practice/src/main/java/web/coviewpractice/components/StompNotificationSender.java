package web.coviewpractice.components;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompNotificationSender {


    private final SimpMessagingTemplate messageTemplate;

    private static final String TOPIC_CALL_NOTIFICATION = "/queue/call/callNotification";
    private static final String TOPIC_CALL_ERROR = "/queue/call/error";

    public void sendCallNotification(Principal principal, Object obj) {
        log.debug("sendLeaveRoomsToUser() - principal: {}, obj: {}", principal, obj);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, obj);
    }

    public void sendError(Principal principal, Object obj) {
        log.debug("sendError() - principal: {}, obj: {}", principal, obj);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_ERROR, obj);
    }
}
