package webrtc.webrtc2phase.service.chat;


import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import webrtc.webrtc2phase.controller.presentations.CreateRoom;
import webrtc.webrtc2phase.dto.ChatRoomMap;
import webrtc.webrtc2phase.dto.ChatType;
import webrtc.webrtc2phase.dto.KurentoRoomDto;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RtcChatService {
    private final KurentoClient kurento;

    public KurentoRoomDto createChatRoom(
            CreateRoom createRoom
    ) {
        KurentoRoomDto room = new KurentoRoomDto();
        String roomId = UUID.randomUUID().toString();
        room.setRoomInfo(
                roomId,
                createRoom.getRoomName(),
                createRoom.getRoomPwd(),
                createRoom.isSecretChk(),
                0,
                createRoom.getMaxUserCnt(),
                ChatType.RTC,
                kurento);

        // 파이프라인 생성
        room.createPipline();

        room.setUserList(new ConcurrentHashMap<>());

        // map 에 채팅룸 아이디와 만들어진 채팅룸을 저장
        ChatRoomMap.getInstance().getChatRooms().put(
                room.getRoomId(),
                room);

        return room;
    }
}
