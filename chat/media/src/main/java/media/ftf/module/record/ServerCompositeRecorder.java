package media.ftf.module.record;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import media.ftf.handler.dto.RecordInfo;
import media.ftf.properties.RecordProperties;
import org.kurento.client.HubPort;
import org.kurento.client.RecorderEndpoint;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Builder
public class ServerCompositeRecorder {

    private final RecordProperties.ConfigProperties configProperties;
    private final RecordProperties.ModeProperties.ModeConfigProperties modeConfigProperties;

    private final Supplier<Composite> createComposite;
    private final Function<RecordInfo, RecorderEndpoint> createRecorderEndpoint;
    private final Supplier<Map<String, Participant>> getParticipants;
    private final Map<String, HubPort> inHupPortMap = new ConcurrentHashMap<>();
    private Consumer<RecordInfo> stopCallback;
    private Composite composite;
    private HubPort outHubPort;
    private RecorderEndpoint serverCompositeRecorderEndpoint;

    private RecordInfo recordInfo;

    public synchronized void start(SessionInfo sessionInfo, Consumer<RecordInfo> startCallback, Consumer<RecordInfo> stopCallback, Consumer<RecordInfo> errorCallback) {

        this.stopCallback = stopCallback;
        this.composite = createComposite.get();
        this.outHubPort = new HubPort.Builder(composite).build();

        final var participants = getParticipants.get();
        participants.values().forEach(this::add);

        final var newRecordInfo = RecordUtils.createRecordInfo(modeConfigProperties.format(), modeConfigProperties.type(), configProperties, sessionInfo);

        serverCompositeRecorderEndpoint = createRecorderEndpoint.apply(newRecordInfo);
        serverCompositeRecorderEndpoint.addRecordingListener(event -> {
            log.info("{}", event);
            startCallback.accept(newRecordInfo);
            this.recordInfo = newRecordInfo;
        });
        serverCompositeRecorderEndpoint.addStoppedListener(event -> {
            log.info("{}", event);
            stopCallback.accept(newRecordInfo);
        });
        serverCompositeRecorderEndpoint.addErrorListener(event -> {
            log.error("errorCode: {}, description: {}", event.getErrorCode(), event.getDescription());
            errorCallback.accept(newRecordInfo);
        });
        serverCompositeRecorderEndpoint.addMediaFlowInStateChangedListener(event -> log.info("{}", event));
        serverCompositeRecorderEndpoint.addMediaFlowOutStateChangedListener(event -> log.info("{}", event));

        RecordUtils.mediaElementConnect(modeConfigProperties.type(), outHubPort, serverCompositeRecorderEndpoint);
        serverCompositeRecorderEndpoint.record();
    }

    public synchronized void add(Participant participant) {

        final var outgoingEp = participant.getOutWebRtcEndpoint();
        if (outgoingEp == null) return;  // 모니터링 유저는 outgoing 이 없음

        final var inHupPort = inHupPortMap.computeIfAbsent(participant.getUserId(), k -> {
            final var newInHubPort = new HubPort.Builder(composite).build();
            newInHubPort.addTag(FermiConstant.TAG_PEER_ID, participant.getUserId() + "-inHub");
            return newInHubPort;
        });
        log.info("{} {}", outgoingEp, inHupPort);
        RecordUtils.mediaElementConnect(modeConfigProperties.type(), outgoingEp, inHupPort);
    }

    public synchronized void add(String userId) {
        final var participants = getParticipants.get();
        final var participant = participants.get(userId);
        add(participant);
    }

    public synchronized void remove(String userId) {
        final var inHubPort = inHupPortMap.remove(userId);
        inHubPort.release();
    }

    public synchronized void stop() {

        RecordUtils.mediaElementDisconnect(modeConfigProperties.type(), outHubPort, serverCompositeRecorderEndpoint);

        serverCompositeRecorderEndpoint.stopAndWait();
        serverCompositeRecorderEndpoint.release();
        serverCompositeRecorderEndpoint = null;

        outHubPort.release();
        outHubPort = null;

        inHupPortMap.forEach((userId, inHubPort) -> remove(userId));

        composite.release();
        composite = null;
        recordInfo = null;
    }

    public synchronized void stopWithKurentoDown() {
        if (this.recordInfo != null) stopCallback.accept(this.recordInfo);
    }

}
