package webchat.webrtc3phase.domain;

import lombok.Data;

import java.time.LocalDateTime;



@Data
public class ChattingUser {
    private String name;
    private String ipAddress;
    private String joinTime;

    public void enterRoom() {

    }

    public void enterWaitingRoom() {

    }
}
