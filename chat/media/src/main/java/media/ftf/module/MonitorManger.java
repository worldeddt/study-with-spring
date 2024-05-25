package media.ftf.module;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.domain.RoomManager;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class MonitorManger {

    private final RoomManager roomManager;
    private final Map<String, MonitorSession> userId_monitorSession = new ConcurrentHashMap<>();


    // 이곳에서
    public synchronized void joinRoom(String userId, String roomId, Consumer<Room> joinCallback) {
        final var monitorSession = userId_monitorSession.computeIfAbsent(userId, MonitorSession::new);
        monitorSession.addRoom(roomId);

        roomManager.handleRoom(roomId, room -> {
            room.joinWithOnlyIncoming(userId);
            joinCallback.accept(room);
        });
    }

    public synchronized void leaveRoom(String userId, String roomId) {
        final var monitorSession = userId_monitorSession.get(userId);
        if (monitorSession != null) monitorSession.removeRoom(roomId);
        roomManager.handleRoom(roomId, room -> room.leave(userId));
    }

    public synchronized void leaveAllRoom(String userId) {
        final var monitorSession = userId_monitorSession.remove(userId);
        if (monitorSession != null)
            monitorSession.getSet().forEach(roomId -> roomManager.handleRoom(roomId, room -> room.leave(userId)));
    }

    public synchronized void endRoomProcess(String roomId) {
        // room 참여 인덱스 생성 필요 (모두 순환 방지를 위한)
        userId_monitorSession.forEach((k, v) -> v.removeRoom(roomId));
    }

}
