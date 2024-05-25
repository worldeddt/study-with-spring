package media.ftf.module;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class MonitorSession {

    private final String monitoringUserId;
    private final Set<String> roomIdSet = new HashSet<>();

    public void addRoom(String roomId) {
        roomIdSet.add(roomId);
    }

    public void removeRoom(String roomId) {
        roomIdSet.remove(roomId);
    }

    public Set<String> getSet() {
        return Collections.unmodifiableSet(roomIdSet);
    }

}
