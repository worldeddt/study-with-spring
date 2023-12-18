package webchat.webrtc3phase.controller.dto;

import lombok.*;
import lombok.experimental.Accessors;
import webchat.webrtc3phase.dto.ChatRoomDto;
import webchat.webrtc3phase.dto.ChatRoomMap;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindUserName {
    private String userUUID;
    private String roomId;
    private ConcurrentMap<String, ChatRoomDto> chatRooms;

    public ChatRoomDto getChatRoom() {
        return chatRooms.get(roomId);
    }

    public static FindUserName init(
            String userUUID,
            String roomId,
            ConcurrentMap<String, ChatRoomDto> chatRooms
    ) {
        return FindUserName.builder()
                .roomId(roomId)
                .userUUID(userUUID)
                .chatRooms(chatRooms)
                .build();
    }

    public static FindUserName init(
            String userUUID,
            String roomId
    ) {
        return FindUserName.builder()
                .roomId(roomId)
                .userUUID(userUUID)
                .build();
    }
}
