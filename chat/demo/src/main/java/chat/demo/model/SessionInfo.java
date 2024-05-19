package chat.demo.model;

import chat.demo.enums.EUserType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.index.Indexed;

import java.security.Principal;

@EqualsAndHashCode
@ToString
@Getter
@Builder
public class SessionInfo {

    private String userId;
    @Indexed
    private String principalName;
    private String userName;
    private EUserType userType;
    private boolean isHost;

}