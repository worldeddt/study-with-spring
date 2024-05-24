package media.ftf.interceptors;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.advice.CommonException;
import media.ftf.advice.dto.ErrorMessage;
import media.ftf.application.interfaces.SocketSessionService;
import media.ftf.enums.CommonCode;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
@Component
public class InboundChannelInterceptor implements ChannelInterceptor {

    private final SocketSessionService socketSessionServiceImpl;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final var accessor = StompHeaderAccessor.wrap(message);
        final var principal = accessor.getUser();
        final var command = accessor.getCommand();

        try {
            if (command == StompCommand.CONNECT) {
                final var bearerToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
                log.info("==== stomp connect ====");
                socketSessionServiceImpl.register(principal, bearerToken);
            }
        } catch (CommonException e) {
            log.warn("", e);
            final var errorCode = e.getCommonCode();
            final var errorMessage = ErrorMessage.of(errorCode);
            final var jsonString = errorMessage.toJsonString();
            final var base64Str = Base64.getEncoder().encodeToString(jsonString.getBytes());
            throw new MessagingException(base64Str);
        } catch (Exception e) {
            log.error("", e);
            final var errorCode = CommonCode.INTERNAL_SERVER_ERROR;
            final var errorMessage = ErrorMessage.of(errorCode);
            final var jsonString = errorMessage.toJsonString();
            final var base64Str = Base64.getEncoder().encodeToString(jsonString.getBytes());
            throw new MessagingException(base64Str);
        }

        return message;
    }

}
