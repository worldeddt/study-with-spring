package media.ftf.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RecordStatus {

    STATUS_START("START", "START"),
    STATUS_STOP("STOP", "STOP"),
    STATUS_ERROR("ERROR", "ERROR"),
    STATUS_ENC_START("ENC_START", "ENC_START"),
    STATUS_ENC_FINISH("ENC_FINISH", "ENC_FINISH"),
    STATUS_ENC_ERROR("ENC_ERROR", "ENC_ERROR");

    private final String value;
    private final String description;
    private static final Map<String, String> valueIndex;

    static {
        valueIndex = Arrays.stream(RecordStatus.values())
                .collect(Collectors.toUnmodifiableMap(RecordStatus::getValue, Enum::name));
    }

    public static RecordStatus convert(String value) {
        final var name = valueIndex.get(value);
        return RecordStatus.valueOf(name);
    }
}