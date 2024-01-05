package webchat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import webchat.controller.dto.ChatDto;
import webchat.timer.KurentoRegisterScheduler;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    @Autowired
    private KurentoRegisterScheduler kurentoRegisterScheduler;

    private final SimpMessageSendingOperations template;

    @MessageMapping("/ws/*")
    public synchronized void message(@Payload Object object) {
        log.info("websocket message : {}", object);
    }
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto) {
        kurentoRegisterScheduler.startTimer();
        template.convertAndSend("/sub/chat/room2/"+chatDto.getRoomId(), chatDto);
        log.info("chat sender = {}", chatDto.getMessage());
    }
}
