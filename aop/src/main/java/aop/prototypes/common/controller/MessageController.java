package aop.prototypes.common.controller;


import aop.prototypes.common.controller.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

    private final RedisTemplate<String, ChatMessage> redisTemplateChatMessage;
    private final SimpMessageSendingOperations messagingTemplate;
    private final String channel = "room1";

    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws JsonProcessingException {
        log.info("dadfadfadfadfadf : {}", new ObjectMapper().writeValueAsString(chatMessage));
        messagingTemplate.convertAndSend("/topic/public/"+chatMessage.getRoomId(), chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.convertAndSend")
    public void senMessage(@Payload ChatMessage chatMessage) {

    }

    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
