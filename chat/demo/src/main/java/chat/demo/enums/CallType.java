package chat.demo.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CallType {

    OUTBOUND_CLIENT("OUTBOUND_CLIENT", ""),
    OUTBOUND_COUNSELOR("OUTBOUND_COUNSELOR", ""),
    INBOUND("INBOUND", "");

    private static final Set<String> nameIndex;

    @JsonValue
    private final String value;
    private final String msg;

    static {
        nameIndex = Arrays.stream(CallType.values())
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static CallType convert(String value) {
        if (value == null) return null;
        if (!nameIndex.contains(value)) return null;
        return CallType.valueOf(value);
    }
}
