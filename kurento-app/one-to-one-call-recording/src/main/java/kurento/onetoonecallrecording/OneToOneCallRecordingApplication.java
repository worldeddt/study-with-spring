package kurento.onetoonecallrecording;

import kurento.onetoonecallrecording.messinger.CallHandler;
import kurento.onetoonecallrecording.user.UserRegistry;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Slf4j
@EnableWebSocket
@SpringBootApplication
public class OneToOneCallRecordingApplication implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(OneToOneCallRecordingApplication.class, args);
    }

    @Bean
    public CallHandler callHandler() {
        return new CallHandler();
    }

    @Bean
    public UserRegistry registry() {
        return new UserRegistry();
    }

    @Bean
    public KurentoClient kurentoClient() {
        return new KurentoClientBuilder()
                .setKmsWsUri("ws://localhost:8888/kurento")
                .onDisconnected(
                () -> {
                    log.info("disconnect !!!");
                }
        ).onConnected(
                () -> {
                    log.info("connect success!!!");
                }
        ).connect();
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(callHandler(), "/call")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*");
    }

}
