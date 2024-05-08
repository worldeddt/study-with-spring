package media.ftf.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig extends StompSessionHandlerAdapter implements WebSocketMessageBrokerConfigurer {

    private final TaskScheduler heartBeatScheduler;
    private final MyHandsShakeHandler myHandsShakeHandler;
    private final InboundChannelInterceptor inboundChannelInterceptor;
    private final OutboundChannelInterceptor outboundChannelInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/media")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue") // chat  구독 시 사용
                .setTaskScheduler(heartBeatScheduler)
                .setHeartbeatValue(new long[]{5 * 1000, 5 * 1000});


        registry
                .setApplicationDestinationPrefixes("/pub") // chat ...
                .setUserDestinationPrefix("/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(160 * 64 * 1024);
        registration.setSendTimeLimit(20 * 10000);
        registration.setSendBufferSizeLimit(10 * 512 * 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(inboundChannelInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(outboundChannelInterceptor);
    }

}
