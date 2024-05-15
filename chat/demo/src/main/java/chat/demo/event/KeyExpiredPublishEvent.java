package chat.demo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Builder
@Getter
public class KeyExpiredPublishEvent {
    private final String callId;
    private final String inviteKey;
    private final String principalName;
}
