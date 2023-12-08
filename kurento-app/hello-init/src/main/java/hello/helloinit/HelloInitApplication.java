package hello.helloinit;

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
public class HelloInitApplication implements WebSocketConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(HelloInitApplication.class, args);
	}

	@Bean
	public HelloWorldHandler handler()
	{
		return new HelloWorldHandler();
	}

	@Bean
	public KurentoClient kurentoClient() throws InterruptedException {
		log.info("잔다.");
		Thread.sleep(2000);
		log.info("일어났다.");

		KurentoClient kurentoClient = new KurentoClientBuilder()
				.setKmsWsUri("ws://localhost:8888/kurento")
				.onConnected(() -> {
					log.debug("안녕 난 접속을 했어");
				})
				.onDisconnected(() -> {
					log.debug("안녕 난 접속을 끊었어");
				})
				.connect();

		log.info("kurento client = {}", kurentoClient.getLabel());

		return kurentoClient;
	}

	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(32768);
		return container;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
	{
		registry.addHandler(handler(), "/helloworld");
	}

}
