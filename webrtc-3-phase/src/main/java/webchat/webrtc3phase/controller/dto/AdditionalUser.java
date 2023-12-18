package webchat.webrtc3phase.controller.dto;

import lombok.*;
import webchat.webrtc3phase.dto.ChatRoomDto;

import java.util.concurrent.ConcurrentMap;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalUser {

    private String username;
    private String roomId;
    private ConcurrentMap<String, ChatRoomDto> chatRooms;

    public ChatRoomDto getChatRoom() {
        return chatRooms.get(roomId);
    }

    public static AdditionalUser init(
            String username,
            String roomId,
            ConcurrentMap<String, ChatRoomDto> chatRooms
    ) {
        return AdditionalUser.builder()
                .roomId(roomId)
                .username(username)
                .chatRooms(chatRooms)
                .build();
    }

    public static AdditionalUser init(
            String username,
            String roomId
    ) {
        return AdditionalUser.builder()
                .roomId(roomId)
                .username(username)
                .build();
    }
    
}
