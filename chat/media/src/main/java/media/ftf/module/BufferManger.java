package media.ftf.module;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class BufferManger {

    // roomId userId present

    private final Map<BufferKey, CandidateBuffer> bufferMap = new ConcurrentHashMap<>();
    private final Map<String, Set<BufferKey>> userIdIndex = new ConcurrentHashMap<>();


    public synchronized CandidateBuffer getCandidateBuffer(String roomId, String userId, String present) {
        final var key = BufferKey.builder().roomId(roomId).userId(userId).present(present).build();
        return bufferMap.computeIfAbsent(key, k -> {
            final var buffer = new CandidateBuffer();
            final var indexSet = userIdIndex.computeIfAbsent(userId, k2 -> new HashSet<>());
            indexSet.add(key);
            return buffer;
        });
    }

    public synchronized void release(String userId) {
        final var set = userIdIndex.get(userId);
        if (set != null) {
            set.forEach(bufferMap::remove);
        }
    }

    @ToString
    @Builder
    @EqualsAndHashCode
    public static class BufferKey {
        private String roomId;
        private String userId;
        private String present;
    }
}