package chat.demo.repository.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "session", timeToLive = 86400)
public class SessionCache {


    @Indexed
    private String principalName;
}
