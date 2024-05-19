package chat.demo.repository.entity;


import chat.demo.enums.EUserType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.security.Principal;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@RedisHash(value = "session", timeToLive = 86400)
@ToString
public class SessionCache {

    @Indexed
    private String userId;
    @Indexed
    private String server;

    private String userName;

    private EUserType userType;

    @Indexed
    private String loginId;
    private String principalName;

    private boolean isHost;
}
