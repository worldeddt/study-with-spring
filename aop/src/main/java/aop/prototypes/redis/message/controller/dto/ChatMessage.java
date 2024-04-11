package aop.prototypes.redis.message.controller.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private LocalDateTime timestamp;

    // Getters and setters

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}
