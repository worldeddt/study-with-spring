package media.ftf.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.WebRtcEndpointType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerMessage {
    private String roomId;
    private WebRtcEndpointType type;
    private String present;
    private String sdp;
}