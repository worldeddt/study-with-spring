package webrtcproto2.webrtc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import webrtcproto2.webrtc.handler.SocketHandler;


@Configuration
public class WebSocketConfig implements WebSocketConfigurer {


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(, "/web-rtc").setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }


    @Bean
    public WebSocketHandler socketHandler() {
        return new SocketHandler();
    }
}
