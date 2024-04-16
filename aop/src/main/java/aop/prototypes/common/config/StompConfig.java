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
        log.info("register stomp endpoints init");
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic") // 구독
                 .setTaskScheduler(heartBeatScheduler)
                .setHeartbeatValue(new long[]{5 * 1000, 5 * 1000})
        ; // 구독시 사용
        registry.setApplicationDestinationPrefixes("/pub"); // pub 시 사용
        registry.setUserDestinationPrefix("/user");
    }
}