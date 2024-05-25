package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberNotificationType {

    ENTER("enter", "enter code"),
    LEAVE("leave", "leave code");

    @JsonValue
    private final String value;
    private final String description;
}