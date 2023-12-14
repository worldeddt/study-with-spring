package webchat.webrtc3phase.infra;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisServer {
    private final RedisTemplate<String, Object> redisTemplate
            = new RedisTemplate<>();



}
