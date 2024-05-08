package chat.demo.controller.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class ChatMessage {
    private String userId;
    private String message;
    private String type;
}
