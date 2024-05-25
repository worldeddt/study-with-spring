package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KurentoStatus {

    CD("CD", "연결됨"),
    LD("LD", "연결중"),
    DC("DC", "연결 실패");

    @JsonValue
    private final String value;
    private final String description;
}
