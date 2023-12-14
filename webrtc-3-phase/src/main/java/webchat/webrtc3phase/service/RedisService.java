package webchat.webrtc3phase.service;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Getter
@Service
public class RedisService {
    protected RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOps;
    private ZSetOperations<String, Object> zsetOps;

    @Autowired
    private void RedisService(final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        this.valueOps = redisTemplate.opsForValue();
        this.zsetOps = redisTemplate.opsForZSet();
    }

    public String getStringByKey(String key) {
        return (String)valueOps.get(key);
    }

    public void setStringByKey(String key, Object value) {
        valueOps.set(key, value);
    }
}
