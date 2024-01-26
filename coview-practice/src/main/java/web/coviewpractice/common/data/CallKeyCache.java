package web.coviewpractice.common.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "callKey")
public class CallKeyCache {

    @Id
    private String userId;
    @Indexed
    private String callId;

}
