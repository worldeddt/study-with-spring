package media.ftf.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class MediaCache<T> {
    private final LinkedHashMap<T, Consumer<T>> cache;
    private final Map<T, Long> expirationTimes;
    private final ScheduledExecutorService executorService;

    public MediaCache() {
        cache = new LinkedHashMap<>();
        expirationTimes = new LinkedHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * @param key      키값
     * @param value    키값이 지워지면서 실행되는 함수 혹은 소비되는 값
     * @param ttl      시간
     * @param timeUnit 단위
     */
    public synchronized void put(T key, Consumer<T> value, long ttl, TimeUnit timeUnit) {
        log.info("put key={}, value={}, ttl={}, timeUnit={}", key, value, ttl, timeUnit);
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        cache.put(key, value);
        expirationTimes.put(key, expirationTime);
        scheduleExpiration(key, ttl, timeUnit);
    }

    public synchronized Consumer<T> get(T key) {
        if (!cache.containsKey(key)) {
            return null;
        }
        if (isExpired(key)) {
            remove(key);
            return null;
        }
        return cache.get(key);
    }

    public synchronized Consumer<T> remove(T key) {
        log.info("removing ttl cache ::: key={}", key);
        final var removedValue = cache.remove(key);
        expirationTimes.remove(key);
        return removedValue;
    }

    public synchronized void clear() {
        cache.clear();
        expirationTimes.clear();
    }

    private synchronized boolean isExpired(T key) {
        long currentTime = System.currentTimeMillis();
        return expirationTimes.containsKey(key) && expirationTimes.get(key) <= currentTime;
    }

    private synchronized void scheduleExpiration(T key, long ttl, TimeUnit timeUnit) {
        executorService.schedule(() -> {
            if (isExpired(key)) {
                final var removedValue = remove(key);
                removedValue.accept(key);
            }
        }, ttl, timeUnit);
    }
}
