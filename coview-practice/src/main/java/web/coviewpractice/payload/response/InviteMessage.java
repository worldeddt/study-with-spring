package web.coviewpractice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import web.coviewpractice.common.enums.NotificationType;

import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class InviteMessage implements Serializable {
    private NotificationType type;
    private String inviteKey;
}
