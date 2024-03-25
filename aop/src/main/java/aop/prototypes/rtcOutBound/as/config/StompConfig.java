package aop.prototypes.rtcOutBound.as.config;


import aop.prototypes.rtcOutBound.as.handler.MyDefaultHandshakeHandler;
import aop.prototypes.rtcOutBound.as.interceptors.InboundChannelInterceptor;
import aop.prototypes.rtcOutBound.as.interceptors.OutboundChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
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

    private final MyDefaultHandshakeHandler myDefaultHandshakeHandler;
    private final InboundChannelInterceptor inboundChannelInterceptor;
    private final OutboundChannelInterceptor outboundChannelInterceptor;
    private final TaskScheduler heartBeatScheduler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/call")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(myDefaultHandshakeHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(heartBeatScheduler)
                .setHeartbeatValue(new long[]{5 * 1000, 5 * 1000})
        ; // 구독시 사용
        registry.setApplicationDestinationPrefixes("/pub"); // pub 시 사용
        registry.setUserDestinationPrefix("/user");
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

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // ???????
    }
}
