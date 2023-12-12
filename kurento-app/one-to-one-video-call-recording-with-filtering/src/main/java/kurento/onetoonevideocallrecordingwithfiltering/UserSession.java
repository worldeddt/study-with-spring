package kurento.onetoonevideocallrecordingwithfiltering;


import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class UserSession {
    private final WebSocketSession webSocketSession;
    private final String name;
    private String sdpOffer;
    private String callingTo;
    private String callingFrom;
    private WebRtcEndpoint webRtcEndpoint;
    private WebRtcEndpoint playingWebRtcEndpoint;

    private final List<IceCandidate> candidateList
            = new ArrayList<>();

    public UserSession(WebSocketSession webSocketSession, String name) {
        this.webSocketSession = webSocketSession;
        this.name = name;
    }

    public WebSocketSession getSession() {
        return webSocketSession;
    }

    public String getName() {
        return name;
    }

    public String getSdpOffer() {
        return sdpOffer;
    }

    public void setSdpOffer(String sdpOffer) {
        this.sdpOffer = sdpOffer;
    }

    public String getCallingTo() {
        return callingTo;
    }

    public void setCallingTo(String callingTo) {
        this.callingTo = callingTo;
    }

    public String getCallingFrom() {
        return callingFrom;
    }

    public void setCallingFrom(String callingFrom) {
        this.callingFrom = callingFrom;
    }

    public String getSessionId() {
        return webSocketSession.getId();
    }

    public void sendMessage(JsonObject message) throws IOException {
        log.info("Sending message from user '{}' : '{}'", name, message);
        webSocketSession.sendMessage(new TextMessage(message.toString()));
    }

    public void setWebRtcEndpoint(WebRtcEndpoint webRtcEndpoint) {
        this.webRtcEndpoint = webRtcEndpoint;

        if (this.webRtcEndpoint != null) {
            for (IceCandidate e : candidateList) {
                this.webRtcEndpoint.addIceCandidate(e);
            }
            this.candidateList.clear();
        }
    }

    public void addCandidate(IceCandidate candidate) {
        if (this.webRtcEndpoint != null) {
            this.webRtcEndpoint.addIceCandidate(candidate);
        } else {
            candidateList.add(candidate);
        }

        if (this.playingWebRtcEndpoint != null) {
            this.playingWebRtcEndpoint.addIceCandidate(candidate);
        }
    }

    public WebRtcEndpoint getPlayingWebRtcEndpoint() {
        return playingWebRtcEndpoint;
    }

    public void setPlayingWebRtcEndpoint(WebRtcEndpoint playingWebRtcEndpoint) {
        this.playingWebRtcEndpoint = playingWebRtcEndpoint;
    }

    public void clear() {
        this.webRtcEndpoint = null;
        this.candidateList.clear();
    }
}
