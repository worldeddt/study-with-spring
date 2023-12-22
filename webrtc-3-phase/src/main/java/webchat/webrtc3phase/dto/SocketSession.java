package webchat.webrtc3phase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
// for local server(AS)
public class SocketSession {

    private String sessionId;
    private WebSocketSession webSocket;

    public SocketSession(final String sessionId, WebSocketSession webSocket) {
        this.sessionId = sessionId;
        this.webSocket = webSocket;
    }
}
