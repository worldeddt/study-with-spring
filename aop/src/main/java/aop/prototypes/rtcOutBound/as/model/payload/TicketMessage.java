package aop.prototypes.rtcOutBound.as.model.payload;

import aop.prototypes.rtcOutBound.as.common.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class TicketMessage implements Serializable {
    private final NotificationType type = NotificationType.TICKET;
    private TokenResponse ticket;
}
