package kurento.onetomany;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class UserSession {
    private final WebSocketSession session;

    private WebRtcEndpoint webRtcEndpoint;

    public void sendMessage(JsonObject message) throws IOException {

        log.debug("Sending message from user with session id '{}': {}", session.getId(), message);
        session.sendMessage(new TextMessage(message.toString()));
    }

    public void addCandidate(IceCandidate candidate) {
        webRtcEndpoint.addIceCandidate(candidate);
    }
}
