package aop.prototypes.common.config.Interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class InboundChannelInterceptor implements ChannelInterceptor {


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        final var accessor = StompHeaderAccessor.wrap(message);
//        final var user = accessor.getUser();
//        final var command = accessor.getCommand();
//
//        if (command == null) return message;
//
//        try {
//
//            log.info("InboundChannelInterceptor preSend() message: {}", message);
//
//            if (StompCommand.CONNECT.equals(command)) {
//                final var bearerToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
//                sessionService.addSession(user, bearerToken);
//            }
//
//        } catch (FermiException e) {
//            log.warn("", e);
//            final var errorCode = e.getFermiCode();
//            final var errorMessage = ErrorMessage.of(errorCode);
//            final var jsonString = errorMessage.toJsonString();
//            final var base64Str = Base64.getEncoder().encodeToString(jsonString.getBytes());
//            throw new MessagingException(base64Str);
//        } catch (Exception e) {
//            log.error("", e);
//            final var errorCode = FermiCode.INTERNAL_SERVER_ERROR;
//            final var errorMessage = ErrorMessage.of(errorCode);
//            final var jsonString = errorMessage.toJsonString();
//            final var base64Str = Base64.getEncoder().encodeToString(jsonString.getBytes());
//            throw new MessagingException(base64Str);
//        }
//        return message;
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
