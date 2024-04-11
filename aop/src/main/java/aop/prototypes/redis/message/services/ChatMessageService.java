package aop.prototypes.redis.message.services;


import aop.prototypes.redis.message.config.RedisConfig;
import aop.prototypes.redis.message.controller.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private  final RedisTemplate<String, ChatMessage> redisTemplate;


    @PostMapping("/api/messages")
    public void saveMessage(@RequestBody ChatMessage message) {
        String key = "chat:message";
        redisTemplate.opsForList().leftPush(key, message);
    }


    @GetMapping("/api/messages")
    public List<ChatMessage> getMessages() {
        String key = "chat:messages";
        Long size = redisTemplate.opsForList().size(key);
        return redisTemplate.opsForList().range(key, 0, size);
    }
}
