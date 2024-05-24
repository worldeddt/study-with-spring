package media.ftf.module;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import media.ftf.enums.WebRtcEndpointType;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
public class Participant {  // 소켓 연결 시 생성 하도록 수정

    @Getter
    private final String userId;
    private final Supplier<MediaPipeline> supplier;

    private final WebRtcEndpoint outWebRtcEndpoint;
    private final Map<String, WebRtcEndpoint> present_inWebRtcPeer = new ConcurrentHashMap<>();
    private final Map<String, WebRtcEndpoint> vid_outWebRtcPeer = new ConcurrentHashMap<>();
    private final Map<String, WebRtcEndpoint> vid_inWebRtcPeer = new ConcurrentHashMap<>();
    // Agent 컴포지트 녹화용 + 시그널링 과정 추가되어야 한다.
    private WebRtcEndpoint agentOutWebRtcEndpoint;

    public Participant(String userId, Supplier<MediaPipeline> supplier, boolean useOutEp) {
        this.userId = userId;
        this.supplier = supplier;
        this.outWebRtcEndpoint = useOutEp ? new WebRtcEndpoint.Builder(supplier.get()).build() : null;
    }

    public synchronized WebRtcEndpoint getOutWebRtcEndpoint() {
        return outWebRtcEndpoint;
    }

    public synchronized WebRtcEndpoint getAgentOutWebRtcEndpoint() {
        return agentOutWebRtcEndpoint;
    }

    public synchronized WebRtcEndpoint createInEp(String present) {
        return present_inWebRtcPeer.computeIfAbsent(present, k -> new WebRtcEndpoint.Builder(supplier.get()).build());
    }

    public synchronized WebRtcEndpoint getWebRtcPeerDefaultCreate(WebRtcEndpointType webRtcEndpointType, String present) {
        return switch (webRtcEndpointType) {
            case OUTGOING -> getOutWebRtcEndpoint();
            case INCOMING -> createInEp(present);
            case CUSTOM_OUTGOING -> null;
            case CUSTOM_INCOMING -> null;
        };
    }

    public synchronized void removeInWebRtcPeer(String present) {
        final var webRtcPeer = present_inWebRtcPeer.remove(present);
        if (webRtcPeer != null) {
            log.info("Participant userId:{} 의 inWebRtcPeer present: {} 를 release!", userId, present);
            webRtcPeer.release();
        }
    }

    public synchronized void release() {
        if (outWebRtcEndpoint != null) {
            log.info("Participant userId:{} 의 outWebRtcPeer를 release!", userId);
            outWebRtcEndpoint.release();
        }
        present_inWebRtcPeer.keySet().forEach(this::removeInWebRtcPeer);
    }

}
