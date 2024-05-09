package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebRtcEndpointType {

    OUTGOING("OUTGOING", "기본 회의 전송용 EP TYPE"),
    INCOMING("INCOMING", "기본 회의 수신용 EP TYPE"),
    CUSTOM_OUTGOING("CUSTOM_OUTGOING", "추가 전송용 EP TYPE"),
    CUSTOM_INCOMING("CUSTOM_INCOMING", "추가 회의 수신용 EP TYPE");

    @JsonValue
    private final String value;
    private final String description;
}
