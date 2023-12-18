package webchat.webrtc3phase.controller.dto;

import lombok.*;
import webchat.webrtc3phase.domain.Room;

import java.util.List;




@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomList {
    private List<String> list;

    public static RoomList init(List<String> list) {
        return RoomList.builder()
                .list(list)
                .build();
    }
}
