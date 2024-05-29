package media.ftf.handler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import media.ftf.enums.WebRtcEndpointType;
import org.kurento.client.IceCandidate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandidateMessage {

    private String roomId;
    private WebRtcEndpointType type;
    private String present;
    private IceCandidate iceCandidate;

    @Override
    public String toString() {
        return "roomId:" + roomId + " mode:" + type + " present:" + present + " iceCandidate:" + (
                iceCandidate.getCandidate().toLowerCase().contains("relay") ?
                        "***** TURN RELAY ***** " + iceCandidate.getCandidate() : iceCandidate.getCandidate());
    }
}