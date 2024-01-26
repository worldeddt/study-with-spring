package web.coviewpractice.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class FermiUtil {
    public static String generateShortUUID() {
        final UUID uuid = UUID.randomUUID();
        final long mostSigBits = uuid.getMostSignificantBits();
        final long leastSigBits = uuid.getLeastSignificantBits();

        // 최상위 8바이트와 최하위 8바이트를 조합하여 16자리의 짧은 UUID 생성
        final String shortUUID = String.format("%016x%016x", mostSigBits, leastSigBits);

        return shortUUID;
    }

    public static String generateRandomChain(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }
}
