package webchat.webrtc3phase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import webchat.webrtc3phase.handler.ChatHandler;


@Configuration
@EnableWebSocket
@SpringBootApplication
public class Webrtc3PhaseApplication implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Webrtc3PhaseApplication.class, args);
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatHandler();
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/chat")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }
}
