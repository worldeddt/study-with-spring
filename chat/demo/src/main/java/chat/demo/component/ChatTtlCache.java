package chat.demo.component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
@Component
public class ChatTtlCache<T> {

    private Map<T, Consumer<T>> ttlCache;
    private Map<T, Long> expiredTimes;
    private ScheduledExecutorService executorService;


    public ChatTtlCache() {
        ttlCache = new LinkedHashMap<>();
        expiredTimes = new LinkedHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public synchronized void put(T key, Consumer<T> value, long ttl, TimeUnit timeUnit) {
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        ttlCache.put(key, value);
        expiredTimes.put(key, expirationTime);
        generatingScheduling(key, ttl, timeUnit);
    }

    public synchronized Consumer<T> remove(T key) {
        log.info("removing ttl cache ::: key={}", key);
        final var removedValue = ttlCache.remove(key);
        expiredTimes.remove(key);
        return removedValue;
    }


    private synchronized boolean isExpired(T key) {
        long currentTime = System.currentTimeMillis();
        return expiredTimes.containsKey(key) && expiredTimes.get(key) < currentTime;
    }

    private synchronized void generatingScheduling(T key, Long ttl, TimeUnit timeUnit) {
        executorService.schedule(() -> {
            if (isExpired(key)) {
                final var remove = remove(key);
                remove.accept(key);
            }
        }, ttl, timeUnit);
    }
}
