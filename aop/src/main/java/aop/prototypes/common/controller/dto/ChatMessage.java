package aop.prototypes.common.controller.dto;


import aop.prototypes.common.enums.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String roomId;
}
