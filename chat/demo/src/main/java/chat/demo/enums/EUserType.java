package chat.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EUserType {


    GUEST("GUEST", "상담, 고객"),
    HOST("HOST", "상담, 모니터링");

    private static final Set<String> valueSet;

    static {
        valueSet = Arrays.stream(EUserType.values())
                .map(value -> value.value)
                .collect(Collectors.toUnmodifiableSet());
    }

    private final String value;
    private final String description;

    public static boolean containsByValue(String value) {
        return value != null && valueSet.contains(value);
    }

    public static EUserType convert(String str) {
        if (str == null) return null;
        final var upperCaseStr = str.toUpperCase();
        if (!containsByValue(upperCaseStr)) return null;
        return EUserType.valueOf(upperCaseStr);
    }

}
