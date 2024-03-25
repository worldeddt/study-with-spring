package aop.prototypes.rtcOutBound.as.component;


import aop.prototypes.rtcOutBound.as.model.payload.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompNotificationSender {

    private static final String TOPIC_CALL_NOTIFICATION = "/queue/call/callNotification";
    private static final String TOPIC_CALL_ERROR = "/queue/call/error";
    private final SimpMessagingTemplate messageTemplate;

    public void sendCallNotification(Principal principal, Object redisObj) {
        log.debug("sendCallNotification() - principal: {}, redisObj: {}", principal, redisObj);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, redisObj);
    }

    public void sendCallNotification(Principal principal, ExpiredMessage expiredMessage) {
        log.debug("sendCallNotification() - principal: {}, expiredMessage: {}", principal, expiredMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, expiredMessage);
    }

    public void sendCallNotification(Principal principal, ReleaseMessage releaseMessage) {
        log.debug("sendCallNotification() - principal: {}, releaseMessage: {}", principal, releaseMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, releaseMessage);
    }

    public void sendCallNotification(Principal principal, InviteMessage inviteMessage) {
        log.debug("sendCallNotification() - principal: {}, inviteMessage: {}", principal, inviteMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, inviteMessage);
    }

    public void sendCallNotification(Principal principal, TicketMessage ticketMessage) {
        log.debug("sendCallNotification() - principal: {}, ticketMessage: {}", principal, ticketMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, ticketMessage);
    }

    public void sendCallNotification(Principal principal, ErrorMessage errorMessage) {
        log.debug("sendCallNotification() - principal: {}, errorMessage: {}", principal, errorMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_NOTIFICATION, errorMessage);
    }

    public void sendError(Principal principal, ErrorMessage errorMessage) {
        log.debug("sendError() - principal: {}, errorMessage: {}", principal, errorMessage);
        messageTemplate.convertAndSendToUser(principal.getName(), TOPIC_CALL_ERROR, errorMessage);
    }

}
