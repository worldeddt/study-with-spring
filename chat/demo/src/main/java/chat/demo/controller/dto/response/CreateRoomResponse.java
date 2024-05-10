package chat.demo.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class CreateRoomResponse {
    private String roomId;
    private String mediaServer;
    private String multiMediaServer;
}
