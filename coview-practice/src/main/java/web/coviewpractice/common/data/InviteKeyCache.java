package web.coviewpractice.common.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import web.coviewpractice.common.enums.CallType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "inviteKey", timeToLive = 3000)
public class InviteKeyCache {

    @Id
    private String inviteKey;
    @Indexed
    private String userId;
    private CallType callType;

//    @TimeToLive //이렇게 ttl가능
//    public long getTimeToLive() {
//        return new Random().nextLong();
//    }

    public boolean isInviter(String userId){
        return this.userId.equals(userId);
    }

}

