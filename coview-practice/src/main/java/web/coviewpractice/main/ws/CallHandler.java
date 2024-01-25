package web.coviewpractice.main.ws;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;



@Slf4j
@MessageMapping("/call")
public class CallHandler {



    @MessageMapping("/requestCall")
    public void requestCallMessage()  {
        log.info("messages accepted");

    }

}
