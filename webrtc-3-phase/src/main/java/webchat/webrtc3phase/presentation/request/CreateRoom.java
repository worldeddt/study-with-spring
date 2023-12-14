package webchat.webrtc3phase.presentation.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class CreateRoom {
    private String senderId;
    private String subscribeId;
    private String sessionId;
    private String roomName;
    private String password;

    public static CreateRoom init(
        String senderId,
        String subscribeId,
        String sessionId,
        String roomName,
        String password
    ) {
        return CreateRoom.builder()
                .senderId(senderId)
                .subscribeId(subscribeId)
                .sessionId(sessionId)
                .roomName(roomName)
                .password(password)
                .build();
    }
}
