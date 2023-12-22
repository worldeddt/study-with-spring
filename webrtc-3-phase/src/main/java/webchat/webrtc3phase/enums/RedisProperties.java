package webchat.webrtc3phase.enums;


import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {
    public static final String PF_SESSION = "session:";

    public static final String PF_ROOM = "room:";
    public static final String PF_NOTI = "noti:";
    public static final String INFO = "info";

    public static final String COLON = ":";

    public static final String V2_ROOMS = RedisProperties.PF_ROOM + RedisProperties.INFO + RedisProperties.COLON;;

    public static final String PF_SOCK = "socket:";

    public static final String OWN = "owner";
}
