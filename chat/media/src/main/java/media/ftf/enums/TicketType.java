package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum TicketType {

    CONFERENCE("CONFERENCE", "conference"),
    MONITOR("MONITOR", "monitor");

    private static final Set<String> valueSet;

    static {
        valueSet = Arrays.stream(TicketType.values())
                .map(value -> value.value)
                .collect(Collectors.toUnmodifiableSet());
    }

    @JsonValue
    private final String value;
    private final String description;

    public static boolean containsByValue(String value) {
        return value != null && valueSet.contains(value);
    }
}
