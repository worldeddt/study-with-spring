package webchat.handler;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;

@Component
public class CallHandler {

    @MessageMapping("/ws/*")
    public void call() {

    }

}
