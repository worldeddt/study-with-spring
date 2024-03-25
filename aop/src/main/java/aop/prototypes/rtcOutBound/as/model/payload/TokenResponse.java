package aop.prototypes.rtcOutBound.as.model.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private String multiMediaServer;
    private String accessToken;
    private String roomId;
    private String mediaServer;
}


