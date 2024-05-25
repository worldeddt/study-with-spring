package media.ftf.handler.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.WebRtcEndpointType;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferMessage {
    private String roomId;
    private WebRtcEndpointType type;
    private String vid;
    private String present;
    private String sdp;
}