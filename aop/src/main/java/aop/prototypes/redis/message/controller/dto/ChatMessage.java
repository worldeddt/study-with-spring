package aop.prototypes.redis.message.controller.dto;


import aop.prototypes.redis.message.common.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
}
