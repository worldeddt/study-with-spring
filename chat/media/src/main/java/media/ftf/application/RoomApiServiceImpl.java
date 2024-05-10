package media.ftf.application;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.application.interfaces.RoomApiService;
import media.ftf.domain.RoomManager;
import media.ftf.domain.entity.RoomEntity;
import media.ftf.dto.request.RoomRequest;
import media.ftf.dto.response.RoomResponse;
import media.ftf.mapper.RoomMapper;
import media.ftf.module.Room;
import media.ftf.module.SessionManager;
import media.ftf.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomApiServiceImpl implements RoomApiService {

    private final RoomManager roomManager;
    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final SessionManager sessionManager;

    @Transactional
    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {
        log.debug("create room request : {}", roomRequest);

        final Consumer<Room> closeRoomCallback = room -> {
            final var roomId = room.getRoomId();
            room.findParticipants().forEach(i -> {
                final var principal = sessionManager.findPrincipalByUserId(i.getUserId());
                if (principal != null) {
                    roomMessageSender.sendEndRoomMessage(principal, EndRoomMessage.builder().roomId(roomId).build());
                }
            });
            roomRepository.findById(roomId)
                    .ifPresent(roomEntity -> roomRepository.save(roomEntity.toBuilder().closeDate(LocalDateTime.now()).build()));
//            monitorManger.endRoomProcess(roomId);
        };

        final var roomResponse =
                roomManager.createRoom(roomRequest.getRoomId(), null);
        roomRepository.save(roomMapper.toEntity(roomRequest));
        return roomResponse;
    }
}
