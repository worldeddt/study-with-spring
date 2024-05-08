package chat.demo.controller.dto;


import chat.demo.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteMessage {
    private String sender;
    private NotificationType type;
}
