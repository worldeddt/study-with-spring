package aop.prototypes.rtcOutBound.multimedia.application;


import aop.prototypes.kurentoMultiInstance.config.KurentoConfig;
import aop.prototypes.rtcOutBound.multimedia.domain.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

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
