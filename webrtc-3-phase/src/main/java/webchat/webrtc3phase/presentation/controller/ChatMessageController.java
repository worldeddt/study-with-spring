package webchat.webrtc3phase.presentation.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import webchat.webrtc3phase.dto.ChatMessage;


@Slf4j

@Controller
public class ChatMessageController {


    @MessageMapping("/chat/message")
    public void message(ChatMessage chatMessage) {
        log.info("message = {}", chatMessage.getMessage());


    }
}
