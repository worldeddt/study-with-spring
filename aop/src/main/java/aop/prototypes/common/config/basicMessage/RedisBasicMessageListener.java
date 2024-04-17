package aop.prototypes.common.config.basicMessage;


import aop.prototypes.common.controller.dto.ChatMessage;
import aop.prototypes.common.enums.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisBasicMessageListener implements MessageListener {

    @Qualifier("redisForMessage")
    private final RedisTemplate<String, String> redisForMessage;
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;
//    private final ConcurrentHashMap<String, List<ChatMessage>> messageListByRoomId;
    private final String room = "room1";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        try {
            log.info("message : {}", new ObjectMapper().writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            String publishMessage = redisForMessage.getStringSerializer().deserialize(message.getBody());

            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);

            if (roomMessage.getType().equals(MessageType.CHAT)) {
                messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage., chatMessageResponse);
            }

        } catch (Exception e) {
            throw new ChatMessageNotFoundException();
        }


//        redisForMessage.convertAndSend(room, body);
    }
}