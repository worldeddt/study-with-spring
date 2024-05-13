package chat.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum InvolvementType {
    INVITATION("INVITATION", "초대"),
    HANDOVER("HANDOVER", "권한이양");

    private static final Set<String> nameIndex;

    private final String value;
    private final String description;

    static {
        nameIndex = Arrays.stream(InvolvementType.values())
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static boolean containsByValue(String value) {
        return value != null && nameIndex.contains(value);
    }

    public static InvolvementType convert(String str) {
        if (str == null) return null;
        final var upperCaseStr = str.toUpperCase();
        if (!containsByValue(upperCaseStr)) return null;
        return InvolvementType.valueOf(str);
    }
}
