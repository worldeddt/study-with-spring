package webchat.webrtc3phase.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionIdOwner implements Serializable {
    private static final long serialVersionUID = 4959950948694498903L;

    private String subsId;
    private String userId;

    public SessionIdOwner(String subsId, String userId) {
        this.subsId = subsId;
        this.userId = userId;
    }
}
