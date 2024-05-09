package media.ftf.module;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import media.ftf.properties.RecordProperties;
import org.kurento.client.MediaPipeline;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class Room {

    @Getter
    private final String roomId;
    @Getter
    private final String kurentoUrl;
    private final MediaPipeline mediaPipeline;
    private final Consumer<String> joinRoomCallBack;
    private final Consumer<Room> deleteRoomCallBack;

    @Getter
//    private final Recorder recorder;
    private final Map<String, Participant> userId_participants;

    @Builder
    public Room(
            String roomId,
            String kurentoUrl,
            MediaPipeline mediaPipeline,
            Consumer<Room> deleteRoomCallBack,
            Consumer<String> joinRoomCallBack,
            RecordProperties recordProperties
    ) {
        this.roomId = roomId;
        this.kurentoUrl = kurentoUrl;
        this.mediaPipeline = mediaPipeline;
        this.joinRoomCallBack = joinRoomCallBack;
        this.deleteRoomCallBack = deleteRoomCallBack;
        this.userId_participants = new ConcurrentHashMap<>();
//        this.recorder = Recorder.builder()
//                .recordProperties(recordProperties)
//                .getParticipants(() -> userId_participants)
//                .getMediaPipeline(() -> mediaPipeline)
//                .build();
    }

    public synchronized void join(String userId) {
        log.debug("[room][join] roomId: {}, userId: {}", roomId, userId);
        userId_participants.computeIfAbsent(userId, k -> new Participant(userId, () -> mediaPipeline, true));
        this.joinRoomCallBack.accept(roomId);
//        recorder.add(userId);
    }

    public synchronized void joinWithOnlyIncoming(String userId) {
        userId_participants.computeIfAbsent(userId, k -> new Participant(userId, () -> mediaPipeline, false));
    }

    public synchronized void leave(String userId) {
        log.debug("[room][leave] roomId: {}, userId: {}", roomId, userId);
//        recorder.remove(userId);

        final var participant = userId_participants.remove(userId);
        if (participant != null) participant.release();


        if (userId_participants.isEmpty()) {
            release();
        } else {
            userId_participants.values().forEach(otherParticipant -> otherParticipant.removeInWebRtcPeer(userId));
        }

    }

    public synchronized void release() {
        log.debug("[room][release] roomId: {}", roomId);
        // 해당 위치 순서 중요
        deleteRoomCallBack.accept(this);
//        recorder.stop();
        userId_participants.keySet().forEach(this::leave);
        mediaPipeline.release();
    }

    public synchronized void releaseWithKurentoDown() {
        log.debug("[room][release] roomId: {}", roomId);
        // 해당 위치 순서 중요
        deleteRoomCallBack.accept(this);
//        recorder.stopWithKurentoDown();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    public synchronized Participant findParticipant(String userId) {
        return userId_participants.get(userId);
    }


    public synchronized List<Participant> findParticipants() {
        return userId_participants.values().stream()
                .filter(participant -> participant.getOutWebRtcEndpoint() != null)
                .toList();
    }

    public synchronized List<Participant> findOtherParticipants(String userId) {
        return findParticipants().stream()
                .filter(i -> !i.getUserId().equals(userId))
                .filter(i -> i.getOutWebRtcEndpoint() != null)
                .toList();
    }

    public synchronized List<Participant> findOtherParticipantsWithMonitoringUser(String userId) {
        return userId_participants.values().stream()
                .filter(entry -> !entry.getUserId().equals(userId))
                .toList();
    }

}
