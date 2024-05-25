package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EMsgType {

    USER("USER", "일반 채팅"),
    SYSTEM("SYSTEM", "조작 메시지"),
    NOTICE("NOTICE", "서버 알림");

    @JsonValue
    private final String value;
    private final String description;
}