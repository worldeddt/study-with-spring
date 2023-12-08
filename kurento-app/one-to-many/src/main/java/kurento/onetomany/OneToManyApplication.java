package kurento.onetomany;

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
@SpringBootApplication
@EnableWebSocket
public class OneToManyApplication implements WebSocketConfigurer {

	@Bean
	public CallHandler callHandler() {
		return new CallHandler();
	}

	@Bean
	public KurentoClient kurentoClient() {

		KurentoClient kurentoClient = new KurentoClientBuilder()
				.setKmsWsUri("ws://localhost:8888/kurento")
				.onConnected(() -> {
					log.debug("안녕 난 접속을 했어");
				})
				.onDisconnected(() -> {
					log.debug("안녕 난 접속을 끊었어");
				})
				.connect();

		return kurentoClient;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(callHandler(), "/call")
				.setAllowedOrigins("*")
				.setAllowedOriginPatterns("*");
	}

	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(32768);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(OneToManyApplication.class, args);
	}

}
