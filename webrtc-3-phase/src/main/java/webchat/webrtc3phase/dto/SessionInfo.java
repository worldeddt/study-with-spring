package webchat.webrtc3phase.dto;

import lombok.Data;
import webchat.webrtc3phase.enums.EUserType;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public class SessionInfo implements Serializable {
    private static final long serialVersionUID = 4959950948694498903L;

    private String subsId;
    private EUserType userType;
    private String userId;
    private String userName;
    private String sessionId;
    private String loginId;
    // 210804
    private String myAsAddr;
    // 210805
    // key: roomId
    private final ConcurrentMap<String, Boolean> endRoomMap = new ConcurrentHashMap<>();
    // 220325
    private boolean isClosed = false;

}
