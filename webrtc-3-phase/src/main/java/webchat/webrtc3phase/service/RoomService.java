package webchat.webrtc3phase.service;


import com.cedarsoftware.util.io.JsonWriter;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import webchat.webrtc3phase.domain.Room;
import webchat.webrtc3phase.presentation.request.CreateRoom;

import java.util.UUID;

import static webchat.webrtc3phase.enums.RedisProperties.V2_ROOMS;


@Slf4j
@Service
public class RoomService extends RedisService {
    private Gson gson = new Gson();
    private HashOperations<String, String, String> hashOps;
    private HashOperations<String, String, String> hashOps2;

    @PostConstruct
    public void init() {
       this.hashOps = redisTemplate.opsForHash();
    }

    public synchronized Room createRoom(CreateRoom createRoom) {

        Room room1 = createNinit(createRoom.getSubscribeId(), createRoom.getRoomName());
        putRoomById(createRoom.getSubscribeId(), room1.getRoomId(), room1);

        return findRoomById(createRoom.getSubscribeId(), room1.getRoomId());
    }

    private Room createNinit(
            String subsId, String roomName
    ) {
        Room room = new Room();
        room.setSubsId(subsId);
        room.setRoomId(UUID.randomUUID().toString());
        room.setRoomName(roomName);

        return room;
    }

    private synchronized void putRoomById(String subsId, String roomId, Room room) {
        log.info("레디스에 방 데이터 삽입 -> subsId={}, roomId={}, room={}",
                subsId, roomId, JsonWriter.objectToJson(room));
        hashOps.putIfAbsent(V2_ROOMS+subsId, roomId.toString(), gson.toJson(room));
    }

    public synchronized Room findRoomById(String subsId, String roomId) {
        log.info("레디스의 방 데이터 가져옴 -> subsId={}, roomId={}", subsId, roomId);

        return gson.fromJson(hashOps.get(V2_ROOMS+subsId, roomId), Room.class);
    }
}
