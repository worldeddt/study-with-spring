package chat.demo.component;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class CallManager {

    private final Map<String, Set<String>> callMap = new ConcurrentHashMap<>();
    private final Map<String, String> userIdIndex = new ConcurrentHashMap<>();

    public synchronized void put(String callId, String userId) {
        log.info("callId: {}, userId: {}", callId, userId);

        final var set = callMap.computeIfAbsent(callId, key -> new HashSet<>());

        HashSet<Object> objects = new HashSet<>();
        objects.add(userId);

        set.add(userId);
        userIdIndex.put(userId, callId);
    }

    public synchronized void remove(String callId) {
        log.info("callId: {}", callId);
        final var userIdSet = callMap.remove(callId);
        if (userIdSet != null)
            userIdSet.forEach(this::remove);
    }

    public synchronized void removeByUserId(String userId) {
        log.info("userId: {}", userId);
        final var callId = userIdIndex.remove(userId);
        if (callId == null) return;

        final var userSet = callMap.get(callId);

        if (userSet.size() == 1) {
            remove(callId);
        } else {
            userSet.remove(userId);
        }
    }

    public synchronized void handle(String callId, Consumer<Set<String>> consumer) {
        log.info("{} {}", callId, callMap);
        log.info("{}", callMap.get(callId));
        log.info("{}", callMap.getOrDefault(callId, new HashSet<>()));
        final var userSet = callMap.getOrDefault(callId, new HashSet<>());
        consumer.accept(userSet);
    }

}
