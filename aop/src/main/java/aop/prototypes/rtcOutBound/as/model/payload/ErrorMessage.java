package aop.prototypes.rtcOutBound.as.model.payload;


import aop.prototypes.rtcOutBound.as.common.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ErrorMessage implements Serializable {
    private final NotificationType type = NotificationType.ERROR;
    private int status;
    private String code;
    private String message;
    private String destination;
    private String payload;
    private List<String> data;
}
