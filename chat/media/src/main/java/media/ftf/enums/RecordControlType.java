package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RecordControlType {

    START("START", "녹화 시작"),
    STOP("STOP", "녹화 중지");

    @JsonValue
    private final String value;
    private final String description;

}