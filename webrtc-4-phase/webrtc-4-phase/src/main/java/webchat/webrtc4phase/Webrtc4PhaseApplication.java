package webchat.webrtc4phase;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import webchat.config.KurentoConfig;
import webchat.handler.CallHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableWebSocket
@SpringBootApplication
@AllArgsConstructor
public class Webrtc4PhaseApplication implements WebSocketConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(Webrtc4PhaseApplication.class, args);

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// 실행할 Runnable 객체 생성
		Runnable myRunnable = () -> {
			// 실행하고자 하는 작업 내용
			System.out.println("Task is running...");
		};

		myRunnable.run();

		scheduler.scheduleAtFixedRate(myRunnable, 0, 5, TimeUnit.SECONDS);

		// 몇 초 동안 실행을 유지하게 한다. 이 부분이 없으면 프로그램이 즉시 종료됩니다.
		try {
			Thread.sleep(20000); // 30초 동안 실행
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// ScheduledExecutorService 종료
		scheduler.shutdown();
	}

	@Bean
	public WebSocketHandler callHandler() {
		return new CallHandler(new KurentoConfig());
	}

	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(32768);
		return container;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(callHandler(), "/chat")
				.setAllowedOriginPatterns("*")
				.setAllowedOrigins("*");
	}
}
