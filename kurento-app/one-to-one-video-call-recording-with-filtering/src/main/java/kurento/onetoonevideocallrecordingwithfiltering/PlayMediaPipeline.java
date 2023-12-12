package kurento.onetoonevideocallrecordingwithfiltering;


import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static kurento.onetoonevideocallrecordingwithfiltering.CallMediaPipeline.RECORDING_EXT;
import static kurento.onetoonevideocallrecordingwithfiltering.CallMediaPipeline.RECORDING_PATH;


@Slf4j
public class PlayMediaPipeline {

    private final MediaPipeline mediaPipeline;
    private WebRtcEndpoint webRtcEndpoint;
    private final PlayerEndpoint playerEndpoint;

    public PlayMediaPipeline(KurentoClient kurentoClient, String userName, WebSocketSession session) {
        this.mediaPipeline = kurentoClient.createMediaPipeline();

        webRtcEndpoint = new WebRtcEndpoint.Builder(mediaPipeline).build();
        playerEndpoint = new PlayerEndpoint.Builder(
                mediaPipeline, RECORDING_PATH + userName + RECORDING_EXT).build();

        // webRtc 앤드 포인트를 player 앤드 포인트에 연결 설정
        playerEndpoint.connect(webRtcEndpoint);


        playerEndpoint.addErrorListener(new EventListener<ErrorEvent>() {
            @Override
            public void onEvent(ErrorEvent errorEvent) {
                log.info("ErrorEvent : {}", errorEvent.getDescription());
                sendPlayEnd(session);
            }
        });
    }

    public void sendPlayEnd(WebSocketSession webSocketSession) {
        try {
            JsonObject response = new JsonObject();
            response.addProperty("id", "playEnd");
            webSocketSession.sendMessage(new TextMessage(response.toString()));
        } catch (IOException e) {
            log.error("Error sending playEndOfStream message", e);
        }

        mediaPipeline.release();
        this.webRtcEndpoint = null;
    }


    public void play() {
        playerEndpoint.play();
    }
    public String generateSdpAnswer(String sdpOffer) {
        return webRtcEndpoint.processOffer(sdpOffer);
    }

    public MediaPipeline getPipeline() {
        return mediaPipeline;
    }

    public WebRtcEndpoint getWebRtc() {
        return webRtcEndpoint;
    }

    public PlayerEndpoint getPlayer() {
        return playerEndpoint;
    }
}
