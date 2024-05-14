package media.ftf.module;


import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import media.ftf.properties.RecordProperties;
import org.kurento.client.Composite;
import org.kurento.client.MediaPipeline;
import org.kurento.client.RecorderEndpoint;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j

public class Recorder {

    private final String KURENTO_RECORD_PREFIX = "file://";

    private boolean isRecord;
//    private EachRecorder eachRecorder;
//    private ServerCompositeRecorder serverCompositeRecorder;
//    private ClientCompositeRecorder clientCompositeRecorder;
//
//    @Builder
//    public Recorder(
//            RecordProperties recordProperties,
//            Supplier<MediaPipeline> getMediaPipeline,
//            Supplier<Map<String, Participant>> getParticipants
//    ) {
//
//        final var mediaPipeline = getMediaPipeline.get();
//
//        final var eachType = recordProperties.type().each();
//        if (eachType.use()) {
//            this.eachRecorder = EachRecorder.builder()
//                    .configProperties(recordProperties.config())
//                    .modeProperties(eachType)
//                    .getParticipants(getParticipants)
//                    .createRecorderEndpoint(getCreateRecorderEndpointMethod(mediaPipeline, eachType.mode()))
//                    .build();
//        }
//
//        final var serverType = recordProperties.type().serverComposite();
//        if (serverType.use()) {
//            this.serverCompositeRecorder = ServerCompositeRecorder.builder()
//                    .configProperties(recordProperties.config())
//                    .modeProperties(serverType)
//                    .getParticipants(getParticipants)
//                    .createRecorderEndpoint(getCreateRecorderEndpointMethod(mediaPipeline, serverType.mode()))
//                    .createComposite(() -> new Composite.Builder(mediaPipeline).build())
//                    .build();
//        }
//
//        final var clientCompositeType = recordProperties.type().clientComposite();
//        if (clientCompositeType.use()) {
//            this.clientCompositeRecorder = ClientCompositeRecorder.builder()
//                    .configProperties(recordProperties.config())
//                    .modeProperties(clientCompositeType)
//                    .findOutWebEp(userId -> getParticipants.get().get(userId).getOutWebRtcEndpoint())
//                    .createRecorderEndpoint(getCreateRecorderEndpointMethod(mediaPipeline, clientCompositeType.mode()))
//                    .build();
//        }
//
//    }
//
//    private Function<RecordInfo, RecorderEndpoint> getCreateRecorderEndpointMethod(MediaPipeline mediaPipeline, String mode) {
//        return recordInfo -> {
//            final var mediaProfile = RecordUtils.getMediaProfile(mode);
//
//            final var hostDir = recordInfo.getDirPathWithHostPath();
//            RecordUtils.makeDirs(hostDir);
//
//            final var uri = KURENTO_RECORD_PREFIX + recordInfo.getFileUriWithKurentoPath();
//            log.info("{} {}", hostDir, uri);
//            return new RecorderEndpoint.Builder(mediaPipeline, uri).withMediaProfile(mediaProfile).build();
//        };
//    }
//
//    public synchronized boolean start(SessionInfo sessionInfo,
//                                      Function<String, SessionInfo> getSessionInfo,
//                                      Consumer<RecordInfo> startCallback,
//                                      Consumer<RecordInfo> stopCallback,
//                                      Consumer<RecordInfo> errorCallback) {
//        if (isRecord) return false;
//        isRecord = true;
//        if (eachRecorder != null)
//            eachRecorder.start(getSessionInfo, startCallback, stopCallback, errorCallback);
//        if (serverCompositeRecorder != null)
//            serverCompositeRecorder.start(sessionInfo, startCallback, stopCallback, errorCallback);
//        if (clientCompositeRecorder != null)
//            clientCompositeRecorder.start(sessionInfo, startCallback, stopCallback, errorCallback);
//        return true;
//    }
//
//    public synchronized void add(String userId) {
//        if (!isRecord) return;
//        if (eachRecorder != null) eachRecorder.add(userId);
//        if (serverCompositeRecorder != null) serverCompositeRecorder.add(userId);
//    }
//
//    public synchronized void remove(String userId) {
//        if (!isRecord) return;
//        if (eachRecorder != null) eachRecorder.remove(userId);
//        if (serverCompositeRecorder != null) serverCompositeRecorder.remove(userId);
//    }
//
//    public synchronized boolean stop() {
//        if (!isRecord) return false;
//        isRecord = false;
//        if (eachRecorder != null) eachRecorder.stop();
//        if (serverCompositeRecorder != null) serverCompositeRecorder.stop();
//        if (clientCompositeRecorder != null) clientCompositeRecorder.stop();
//        return true;
//    }
//
//    public synchronized void stopWithKurentoDown() {
//        if (!isRecord) return;
//        isRecord = false;
//        if (eachRecorder != null) eachRecorder.stopWithKurentoDown();
//        if (serverCompositeRecorder != null) serverCompositeRecorder.stopWithKurentoDown();
//        if (clientCompositeRecorder != null) clientCompositeRecorder.stopWithKurentoDown();
//    }

}