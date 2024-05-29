package media.ftf.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "media")
public record MediaProperties (
        String serverName,
        WebRtcProperties webrtc,
        int roomDeleteTtl
) {

    public record WebRtcProperties(
            List<String> kurentoUrlList,
            String turnUrl
    ) {

    }

}
