package chat.demo.repository.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "session", timeToLive = 86400)
public class SessionCache {

    @Id
    private String userId;

    @Indexed
    private String principalName;
}
