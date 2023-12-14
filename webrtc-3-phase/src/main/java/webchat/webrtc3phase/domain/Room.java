package webchat.webrtc3phase.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class Room {
    private String subsId;
    private String roomId;
    private String roomName;
    private String password;
    private List<ChattingUser> chattingUsers;

    public void enter() {

    }
}
