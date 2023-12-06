package webrtc.webrtc2phase.config;


import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webrtc.webrtc2phase.rtc.KurentoHandler;
import webrtc.webrtc2phase.service.chat.KurentoManager;
import webrtc.webrtc2phase.service.chat.KurentoUserRegistry;

import java.util.Objects;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebRtcConfig implements WebSocketConfigurer {

    @Value("${kms.url}")
    private String kmsUrl;

    @Bean
    public KurentoClient kurentoClient() {
        String envKmsUrl = System.getenv("KMS_URL");
        if(Objects.isNull(envKmsUrl) || envKmsUrl.isEmpty()){
            return KurentoClient.create(kmsUrl);
        }

        return KurentoClient.create(envKmsUrl);
    }


    private final KurentoUserRegistry registry;

    // room 매니저
    private final KurentoManager roomManager;

    // kurento 를 다루기 위한 핸들러
    @Bean
    public KurentoHandler kurentoHandler(){
        return new KurentoHandler(registry, roomManager);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(kurentoHandler(), "/signal")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }
}
