package media.ftf.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class RoomRequest {
    private String roomId;
    private String userId;
}