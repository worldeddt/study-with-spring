package aop.prototypes.redis.message.services;


import aop.prototypes.redis.message.controller.dto.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageService {

    private final RedisTemplate<String, ChatMessage> redisTemplate;

//    @PostMapping("/api/messages")
//    public void saveMessage(@RequestBody ChatMessage message) throws JsonProcessingException {
//    }


    @GetMapping("/api/messages")
    public ResponseEntity<List<ChatMessage>> getMessages() {
        String key = "chat";
        Long size = redisTemplate.opsForList().size(key);
        return ResponseEntity.ok(redisTemplate.opsForList().range(key, 0, size));
    }
}
