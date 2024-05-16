package chat.demo.application.dto;

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
public class RefuseInviteMessage implements Serializable {
    private final NotificationType type = NotificationType.REFUSE_INVITE;
    private String inviteKey;
    private String userId;
}
