package media.ftf.handler.dto;

import com.fermi.multimedia.core.enums.WebRtcEndpointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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