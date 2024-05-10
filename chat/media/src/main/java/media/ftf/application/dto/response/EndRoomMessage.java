package media.ftf.application.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EndRoomMessage {
    private String roomId;
}
