package aop.prototypes.common.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final TaskScheduler heartBeatScheduler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/topic" 및 "/queue"를 프리픽스로 사용하여 메시지를 브로드캐스팅하고,
        // "/app"을 프리픽스로 사용하여 메시지 핸들러로 라우팅합니다.
        registry.enableSimpleBroker("/topic", "/queue")
                 .setTaskScheduler(heartBeatScheduler)
                .setHeartbeatValue(new long[]{5 * 1000, 5 * 1000})
        ; // 구독시 사용
        registry.setApplicationDestinationPrefixes("/pub"); // pub 시 사용
        registry.setUserDestinationPrefix("/user");
    }
}