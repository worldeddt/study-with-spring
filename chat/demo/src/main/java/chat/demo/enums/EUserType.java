package chat.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EUserType {
    HOST("HOST", "방장"),
    MANAGER("MANAGER", "매니저"),
    CLIENT("CLIENT", "게스트");

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

    public static boolean isAgent(EUserType type) {
        if (type == null) return false;
        return switch (type) {
            case HOST, MANAGER, CLIENT -> true;
            default -> false;
        };
    }
}
