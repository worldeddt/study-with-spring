package webchat.handler;


import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import webchat.config.KurentoConfig;
import webchat.timer.KurentoRegisterScheduler;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallHandler extends TextWebSocketHandler {
    private final Gson gson = new Gson();
    private final KurentoConfig kurentoConfig;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("origin message : {}", message.getPayload());
        kurentoConfig.kurentoRegisterScheduler().startTimer();
//        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
//        log.info("session : {}", session.getId());
//        log.info("message : {}", jsonMessage);

//        String message1 = jsonMessage.get("message").getAsString();
    }
}
