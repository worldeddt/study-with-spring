package media.ftf.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static String generateShortUUID() {
        final UUID uuid = UUID.randomUUID();
        final long mostSigBits = uuid.getMostSignificantBits();
        final long leastSigBits = uuid.getLeastSignificantBits();

        // 최상위 8바이트와 최하위 8바이트를 조합하여 16자리의 짧은 UUID 생성
        final String shortUUID = String.format("%016x%016x", mostSigBits, leastSigBits);

        return shortUUID;
    }

    public static String getExceptionLocation(Exception exception) {

        final var stackTrace = exception.getStackTrace();
        if (stackTrace.length > 0) {
            final var element = stackTrace[0];
            return element.getClassName() + "." + element.getMethodName() + "." + element.getLineNumber();
        } else {
            return "";
        }
    }

    public static class ValueComparator implements Comparator<String> {
        ConcurrentHashMap<String, Float> map;

        public ValueComparator(ConcurrentHashMap<String, Float> map) {
            this.map = map;
        }

        @Override
        public int compare(String key1, String key2) {
            if (map.get(key1) > map.get(key2)) {
                return 1;
            } else if (map.get(key1) < map.get(key2)) {
                return -1;
            } else {
                // 값이 같으면 키를 기준으로 정렬
                return key1.compareTo(key2);
            }
        }
    }
}
