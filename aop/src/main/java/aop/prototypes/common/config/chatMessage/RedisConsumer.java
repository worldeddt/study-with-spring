package aop.prototypes.common.config.chatMessage;

import aop.prototypes.common.controller.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisConsumer implements MessageListener {
    private final RedisTemplate<String, ChatMessage> redisTemplateChatMessage;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        log.info("body chat message : {}", body);

        redisTemplateChatMessage.convertAndSend("chat", body);
    }
}