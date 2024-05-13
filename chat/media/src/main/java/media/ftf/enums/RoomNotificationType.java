package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomNotificationType {

    MEMBER("member", "room member"),
    RECORD("record", "room record"),
    RECORD_CHECK("recordCheck", "room recordCheck"),
    END_ROOM("endRoom", "room end");

    @JsonValue
    private final String value;
    private final String description;
}
