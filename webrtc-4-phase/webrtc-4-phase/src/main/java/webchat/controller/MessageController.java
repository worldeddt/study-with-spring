package webchat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import webchat.controller.dto.ChatDto;
import webchat.timer.KurentoRegisterScheduler;

@Slf4j
@Controller
public class MessageController {

    @Autowired
    private KurentoRegisterScheduler kurentoRegisterScheduler;

    @MessageMapping("/ws/*")
    public synchronized void message(@Payload Object object) {
        log.info("websocket message : {}", object);
    }
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto) {
        kurentoRegisterScheduler.startTimer();
        log.info("chat sender = {}", chatDto.getMessage());
    }
}
