package media.ftf.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KurentoDisconnectedEvent {
    private String kmsUrl;
}

