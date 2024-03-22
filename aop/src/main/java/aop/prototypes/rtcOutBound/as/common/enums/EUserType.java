package aop.prototypes.rtcOutBound.as.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EUserType {

    CLIENT("CLIENT", "상담, 고객"),
    STAFF("STAFF", "상담, 모니터링"),
    MANAGER("MANAGER", "상담, 모니터링"),
    AADMIN("AADMIN", "모니터링"),
    TADMIN("TADMIN", "모니터링"),
    PADMIN("PADMIN", "모니터링"),
    UNKNOWN("UNKNOWN", "모니터링");

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
            case STAFF, MANAGER, AADMIN, TADMIN, PADMIN -> true;
            default -> false;
        };
    }

}
