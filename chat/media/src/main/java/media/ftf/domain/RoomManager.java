package media.ftf.domain;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.advice.CommonException;
import media.ftf.dto.response.RoomResponse;
import media.ftf.enums.CommonCode;
import media.ftf.module.Room;
import media.ftf.properties.MediaProperties;
import media.ftf.properties.RecordProperties;
import media.ftf.utils.MediaCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomManager {
    
    private final ConcurrentHashMap<String, Room> roomIdRooms = new ConcurrentHashMap<>();
    private final MediaProperties mediaProperties;
    private final RecordProperties recordProperties;
    private final Kurento kurento;
    private final MediaCache<String> roomTtl = new MediaCache<>();

    public RoomResponse createRoom(String roomId, Consumer<RoomManager> closeCallback) {

        final var room = roomIdRooms.computeIfAbsent(roomId, k -> {

            final var mediaPipelineWrapper = kurento.createMediaPipeline();

            if (mediaPipelineWrapper == null || mediaPipelineWrapper.getMediaPipeline() == null){
                throw new CommonException(CommonCode.INTERNAL_SERVER_ERROR);
            }

            final var mediaPipeline = mediaPipelineWrapper.getMediaPipeline();

            final var newRoom = Room.builder()
                    .roomId(k)
                    .kurentoUrl(mediaPipelineWrapper.getUrl())
                    .mediaPipeline(mediaPipeline)
                    .recordProperties(recordProperties)
                    .joinRoomCallBack(roomTtl::remove)
                    .deleteRoomCallBack(closingRoom -> {
                        roomIdRooms.remove(closingRoom.getRoomId());
                        roomTtl.remove(closingRoom.getRoomId());
                    })
                    .build();

            roomTtl.put(roomId, s -> newRoom.release(), 5, TimeUnit.MINUTES);

            return newRoom;
        });

        return RoomResponse.builder()
                .roomId(roomId)
                .multiMediaServer(mediaProperties.serverName())
                .mediaServer(room.getKurentoUrl())
                .build();
    }
}
