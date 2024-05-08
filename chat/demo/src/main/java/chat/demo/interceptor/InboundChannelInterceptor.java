package chat.demo.interceptor;


import chat.demo.application.SessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InboundChannelInterceptor implements ChannelInterceptor {

    private final SessionService sessionService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final var accessor = StompHeaderAccessor.wrap(message);
        final var user = accessor.getUser();
        final var command = accessor.getCommand();

        if (command == null) return message;

        try {
            if (command == StompCommand.CONNECT) {
                String authUserId = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
                sessionService.addSession(user, authUserId);
            }

        } catch (MessagingException e) {
            log.warn("", e);
            throw e;
        } catch (Exception e) {
            log.error("", e);
            throw new MessagingException("" + e);
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        log.info("InboundChannelInterceptor afterSendCompletion() message: {}, channel:{}, sent:{}, ex:{}", message, channel, sent, ex);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        log.info("InboundChannelInterceptor postSend() message: {}, channel: {}, sent: {}", message, channel, sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
//        log.info("InboundChannelInterceptor preReceive() channel: {}", channel);
        return true;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        log.info("InboundChannelInterceptor postReceive() message: {}, channel: {}", message, channel);
        return message;
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        log.info("InboundChannelInterceptor afterReceiveCompletion() message: {}, channel:{}, sent:{}, ex:{}", message, channel, ex);
    }

}
