package aop.prototypes.rtcOutBound.as.model.payload;

import aop.prototypes.rtcOutBound.as.common.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ExpiredMessage {
    private final NotificationType type = NotificationType.KEY_EXPIRED;
    private String callId;
    private String inviteKey;
}

