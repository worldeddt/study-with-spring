package chat.demo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OutboundChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        log.info("OutboundChannelInterceptor preSend() message: {}", message);
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        log.info("OutboundChannelInterceptor afterSendCompletion() message: {}, channel:{}, sent:{}, ex:{}", message, channel, sent, ex);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        log.info("OutboundChannelInterceptor postSend() message: {}, channel: {}, sent: {}", message, channel, sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
//        log.info("OutboundChannelInterceptor preReceive() channel: {}", channel);
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        log.info("OutboundChannelInterceptor postReceive() message: {}, channel: {}", message, channel);
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        log.info("OutboundChannelInterceptor afterReceiveCompletion() message: {}, channel:{}, sent:{}, ex:{}", message, channel, ex);
    }
}
