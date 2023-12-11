package kurento.onetoonevideocallrecordingwithfiltering;

import jakarta.servlet.Servlet;
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
public class OneToOneVideoCallRecordingWithFilteringApplication
    implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(OneToOneVideoCallRecordingWithFilteringApplication.class, args);
    }
    static final String DEFAULT_APP_SERVER_URL = "https://localhost:8443";

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
                .onConnected(() -> {
                    log.debug("안녕 난 접속을 했어");
                })
                .onDisconnected(() -> {
                    log.debug("안녕 난 접속을 끊었어");
                })
                .connect();
    }


    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean containerFactoryBean = new ServletServerContainerFactoryBean();

        containerFactoryBean.setMaxTextMessageBufferSize(32768);
        return containerFactoryBean;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(null, "/call")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }
}
