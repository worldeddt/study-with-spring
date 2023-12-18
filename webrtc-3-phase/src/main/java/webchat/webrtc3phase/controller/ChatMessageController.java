package webchat.webrtc3phase.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import webchat.webrtc3phase.dto.ChatMessage;
import webchat.webrtc3phase.service.RoomService;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final RoomService roomService;

    @GetMapping
    public List<String> message(ChatMessage chatMessage) {
        log.info("message = {}", chatMessage.getMessage());

        return roomService.findRoomAll(chatMessage.getSubsId());
    }
}
