package web.coviewpractice.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import web.coviewpractice.main.ws.interceptor.CoDefaultHandshakeHandler;
import web.coviewpractice.main.ws.interceptor.InboundChannelInterceptor;
import web.coviewpractice.main.ws.interceptor.OutboundChannelInterceptor;


@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig extends StompSessionHandlerAdapter implements WebSocketMessageBrokerConfigurer {

    private final CoDefaultHandshakeHandler coDefaultHandshakeHandler;
    private final InboundChannelInterceptor inboundChannelInterceptor;
    private final OutboundChannelInterceptor outboundChannelInterceptor;
    private final TaskScheduler heartBeatScheduler;

    @Lazy
    public StompConfig(CoDefaultHandshakeHandler coDefaultHandshakeHandler,
                       InboundChannelInterceptor inboundChannelInterceptor,
                       OutboundChannelInterceptor outboundChannelInterceptor,
                       TaskScheduler heartBeatScheduler) {
        this.coDefaultHandshakeHandler = coDefaultHandshakeHandler;
        this.inboundChannelInterceptor = inboundChannelInterceptor;
        this.outboundChannelInterceptor = outboundChannelInterceptor;
        this.heartBeatScheduler = heartBeatScheduler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/call")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(coDefaultHandshakeHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(heartBeatScheduler)
                .setHeartbeatValue(new long[]{5*1000, 5*1000});

        registry.setApplicationDestinationPrefixes("/pub");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(160 * 64 * 1024);
        registration.setSendTimeLimit(20 * 10000);
        registration.setSendBufferSizeLimit(10 * 512 * 1024);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // ???????
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
