package aop.prototypes.rtcOutBound.multimedia.application;


import aop.prototypes.rtcOutBound.multimedia.domain.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Slf4j
@Service
@RequiredArgsConstructor
public class MultiMediaService {

    private final Map<String, Room> roomId_rooms = new ConcurrentHashMap<>();



    public void save(String roomId) {

        roomId_rooms.computeIfAbsent(roomId, id -> {
            return Room.builder()
                    .roomId(roomId)
                    .mediaPipeline(null).build();
        });
    }

    public void get(String roomId) {
        Room room = roomId_rooms.get(roomId);
        log.info("room : {}", room.getRoomId());
    }
}
