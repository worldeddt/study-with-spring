package webchat.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent sessionConnectEvent) {

        FermiConnectMessageParser sh = FermiConnectMessageParser.wrap(sessionConnectEvent.getMessage());

        //todo message 에 따른 핸들링

        log.info("message : "+sh.getSid());


        switch (sh.getSid()) {
            case "test" :
                log.info("test test : {}", sessionConnectEvent.getMessage());
                break;
        }

    }
}
