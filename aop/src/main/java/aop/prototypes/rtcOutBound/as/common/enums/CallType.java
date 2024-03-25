package aop.prototypes.rtcOutBound.as.common.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallType {


    OUTBOUND("OUTBOUND", ""),
    INBOUND("INBOUND", ""),
    HELP("HELP", "");


    @JsonValue
    private final String value;
    private final String msg;
}
