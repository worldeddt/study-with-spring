package web.coviewpractice.common.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import web.coviewpractice.common.enums.AgentStatus;

@ToString
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "agent", timeToLive = 3600 * 24)
public class AgentStatusCache {

    @Id
    private String userId;
    private String callId;
    @Indexed
    private AgentStatus agentStatus;

    private int callCount;
    @Indexed
    private String groupId;
    @Builder.Default
    @Indexed
    private boolean isOwner=false;

//    @TimeToLive //이렇게 ttl가능
//    public long getTimeToLive() {
//        return new Random().nextLong();
//    }

}
