package media.ftf.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
@Builder
public class TokenResponse {
    private String server;
    private String roomId;
    private String accessToken;
}
