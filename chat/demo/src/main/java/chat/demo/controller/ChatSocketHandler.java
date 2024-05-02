package chat.demo.controller;


import chat.demo.controller.dto.InviteMessage;
import chat.demo.enums.NotificationType;
import chat.demo.sender.StompNotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@MessageMapping("/chat")
@RequiredArgsConstructor
@RestController
public class ChatSocketHandler {
    private final StompNotificationSender stompNotificationSender;

    @MessageMapping("/requestCall")
    public void call(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        final var principal = simpMessageHeaderAccessor.getUser();
        log.info("session : {}", simpMessageHeaderAccessor.getUser());

        stompNotificationSender.sendCallNotification(principal.getName(),
                InviteMessage.builder()
                        .notificationType(NotificationType.OUTBOUND_CLIENT_CALL)
                        .sender(principal.getName()).build()
        );
    }
}
