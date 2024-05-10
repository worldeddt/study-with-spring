package chat.demo.controller.dto;


import chat.demo.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class InviteMessage implements Serializable {
    private NotificationType type;
    private String inviteKey;
    private String callId;
    private String sender;
}
