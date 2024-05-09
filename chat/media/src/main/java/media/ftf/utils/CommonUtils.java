package media.ftf.utils;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class CommonUtils {
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
