package webchat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import webchat.controller.dto.ChatDto;

@Slf4j
@Controller
public class MessageController {
    @MessageMapping("/ws/*")
    public synchronized void message(@Payload Object object) {
        log.info("websocket message : {}", object);
    }
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto) {
        log.info("chat sender = {}", chatDto.getMessage());
    }
}
