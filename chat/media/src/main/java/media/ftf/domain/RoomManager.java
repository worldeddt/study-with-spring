package media.ftf.domain;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.advice.CommonException;
import media.ftf.dto.response.RoomResponse;
import media.ftf.enums.CommonCode;
import media.ftf.handler.dto.EndRoomDto;
import media.ftf.module.CommonTtlCache;
import media.ftf.module.KurentoManger;
import media.ftf.module.Room;
import media.ftf.properties.MediaProperties;
import media.ftf.properties.RecordProperties;
import media.ftf.utils.MediaCache;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
@Slf4j
@RequiredArgsConstructor
@Component
public class RoomManager {

    private final MediaProperties fermiProperties;
    private final RecordProperties recordProperties;

    private final KurentoManger kurentoManger;

    private final Map<String, Room> roomId_rooms = new ConcurrentHashMap<>();
    private final CommonTtlCache<String> roomTtl = new CommonTtlCache<>();


    public RoomResponse createRoom(String roomId, Consumer<Room> closeCallback) {

        log.info("room Id before create : {}", roomId);
        final var room = roomId_rooms.computeIfAbsent(roomId, k -> {

            final var mediaPipelineWrapper = kurentoManger.createMediaPipeline();

            if (mediaPipelineWrapper == null || mediaPipelineWrapper.getMediaPipeline() == null) {
                log.error("kurento is all down - roomId: {}", roomId);
                throw new CommonException(CommonCode.NOT_FOUND_VALID_MEDIA_SERVER);
            }

            final var mediaPipeline = mediaPipelineWrapper.getMediaPipeline();

            final var newRoom = Room.builder()
                    .roomId(k)
                    .kurentoUrl(mediaPipelineWrapper.getUrl())
                    .mediaPipeline(mediaPipeline)
                    .recordProperties(recordProperties)
                    .joinRoomCallBack(roomTtl::remove)
                    .deleteRoomCallBack(closingRoom -> {
                        log.info("close : {}", closingRoom.getRoomId());
                        log.debug("close : {}", closingRoom.getRoomId());
                        roomId_rooms.remove(closingRoom.getRoomId());
                        roomTtl.remove(closingRoom.getRoomId());
                        closeCallback.accept(closingRoom);
                    })
                    .build();

            roomTtl.put(roomId, s -> newRoom.release(), fermiProperties.roomDeleteTtl(), TimeUnit.SECONDS);

            return newRoom;
        });

        log.info("containkey room id : {}", roomId_rooms.containsKey(roomId));

        return RoomResponse.builder()
                .roomId(roomId)
                .multiMediaServer(fermiProperties.serverName())
                .mediaServer(room.getKurentoUrl())
                .build();
    }

    public void handleRoom(String roomId, Consumer<Room> handleRoom) {
        log.info("roomId before handle room : {}", roomId);

        final var room = roomId_rooms.get(roomId);

        if (room != null) {
            synchronized (room) {
                handleRoom.accept(room);
            }
        }
    }

    public void iteratorRoomId() {
        roomId_rooms.forEach((s, room) -> {
            log.debug("room id : {}", s);
            log.debug("room : {}", room);
        });
    }

    public void handleRoomWithThrowNotFoundRoom(String roomId, Consumer<Room> handleRoom) {
        log.info("room id before throw : {}", roomId);

        final var room = roomId_rooms.get(roomId);
        if (room == null) {
            throw new CommonException(CommonCode.NOT_FOUND_ROOM);
        }

        synchronized (room) {
            handleRoom.accept(room);
        }
    }

    public void kurentoDown(String kmsUrl) {
        roomId_rooms.values().stream()
                .filter(room -> room.getKurentoUrl().equals(kmsUrl))
                .forEach(Room::releaseWithKurentoDown);
    }

}

