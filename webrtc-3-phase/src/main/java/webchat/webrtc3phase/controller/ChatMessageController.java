package webchat.webrtc3phase.controller;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import webchat.webrtc3phase.controller.dto.AdditionalUser;
import webchat.webrtc3phase.controller.dto.ChatDto;
import webchat.webrtc3phase.controller.dto.FindUserName;
import webchat.webrtc3phase.domain.SessionIdOwner;
import webchat.webrtc3phase.dto.ChatRoomMap;
import webchat.webrtc3phase.dto.SessionInfo;
import webchat.webrtc3phase.infra.UserRepository;
import webchat.webrtc3phase.service.MessageService;
import webchat.webrtc3phase.service.RoomService;
import webchat.webrtc3phase.service.SessionService;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations template;
    private final Gson gson;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {

       log.info("header access = {}", headerAccessor.getDestination());
       log.info("header info = {}", headerAccessor.getHeader("userType"));

        String userUUID = messageService.additionalUser(
                AdditionalUser.init(
                        chat.getSender(), chat.getRoomId(), ChatRoomMap.getInstance().getChatRooms()
                ));

        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + "님 입장 !!");
        template.convertAndSend("/sub/chat/room2/"+chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chat) {
        log.info("chat sender = {}", chat.getSender());
        log.info("chat roomId = {}", chat.getRoomId());
        log.info("chat message = {}", chat.getMessage());
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room2/"+chat.getRoomId(), chat);
    }

    @EventListener
    public synchronized void webSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor
                = SimpMessageHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());

        String sessionId = simpMessageHeaderAccessor.getSessionId();

        if (sessionId == null) return;

        SessionIdOwner socketOwnerBySessionId = userRepository.getSocketOwnerBySessionId(sessionId);

        log.info("session id : {}", sessionId);
        log.info("socket owner : {}", socketOwnerBySessionId);

        if (socketOwnerBySessionId != null) {
            String userId = socketOwnerBySessionId.getUserId();
            SessionInfo sessionByUserId = sessionService.findSessionByUserId(userId);

            if (sessionByUserId != null) {
                sessionByUserId.setClosed(true);
                sessionService.updateSessionByUserId(userId,sessionByUserId);
            }

        } else {
            log.info("session id owner is null +++");
            sessionService.clearSessionInfoBySessionId(sessionId);
        }
    }

    private void sendToOthersInTheRoom(String username, String roomId) {
        log.info("User Disconnected : " + username);

        ChatDto chatDto = ChatDto.builder()
                .messageType(ChatDto.MessageType.LEAVE)
                .sender(username)
                .message(username + " 님 퇴장")
                .build();
        template.convertAndSend("/sub/chat/send"+ roomId, chatDto);
    }
}
