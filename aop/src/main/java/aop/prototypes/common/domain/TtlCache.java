package aop.prototypes.common.domain;


import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class TtlCache<K> {
    private final Map<K, Consumer<K>> cache;
    private final Map<K, Long> expirationTimes;
    private final ScheduledExecutorService executorService;

    public TtlCache() {
        cache = new HashMap<>();
        expirationTimes = new HashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }


    public synchronized void put(K key, Consumer<K> value, long ttl, TimeUnit timeUnit) {
        log.info("put key={}, value={}, ttl={}, timeUnit={}", key, value, ttl, timeUnit);
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        cache.put(key, value);
        expirationTimes.put(key, expirationTime);
        scheduleExpiration(key, ttl, timeUnit);
    }

    public synchronized Consumer<K> get(K key) {
        if (!cache.containsKey(key)) {
            return null;
        }

        if (isExpire(key)) {
            remove(key);
            return null;
        }

        return cache.get(key);

    }

    private synchronized void scheduleExpiration(K key, long ttl, TimeUnit timeUnit) {
        executorService.schedule(() -> {
            if (isExpire(key)) {
                final var removedValue = remove(key);
                removedValue.accept(key);
            }
        }, ttl, timeUnit);
    }

    private synchronized Consumer<K> remove(K key) {
        final var removeValue = cache.remove(key);
        expirationTimes.remove(key);
        return removeValue;
    }

    private synchronized boolean isExpire(K key) {
        long currentTimeMillis = System.currentTimeMillis();
        return expirationTimes.containsKey(key) && expirationTimes.get(key) <= currentTimeMillis;
    }

}
