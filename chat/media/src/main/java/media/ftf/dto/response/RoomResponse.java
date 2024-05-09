package media.ftf.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class RoomResponse {
    private String roomId;
    private String multiMediaServer;
    private String mediaServer;
}
