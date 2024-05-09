package media.ftf.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class MonitoringTokenResponse {
    private String accessToken;
}
