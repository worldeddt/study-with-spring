package aop.prototypes.redis.message.common;

import aop.prototypes.redis.message.controller.dto.ChatMessage;
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
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        redisTemplate.convertAndSend("chat", body);
    }
}