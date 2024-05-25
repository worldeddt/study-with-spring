package media.ftf.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RecordType {

    BOTH("BOTH", "BOTH"),
    VIDEO("VIDEO", "VIDEO"),
    AUDIO("AUDIO", "AUDIO");

    @JsonValue
    private final String value;
    private final String description;

    private static final Map<String, String> valueIndex;

    static {
        valueIndex = Arrays.stream(RecordType.values())
                .collect(Collectors.toUnmodifiableMap(RecordType::getValue, Enum::name));
    }

    public static RecordType convert(String value) {
        final var name = valueIndex.get(value);
        return RecordType.valueOf(name);
    }
}
