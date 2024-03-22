package aop.prototypes.rtcOutBound.as;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;


@Slf4j
@MessageMapping("/call")
public class CallSocketHandler {

    @MessageMapping("/requestCall")
    public void requestCall() {
        log.info("request call ====");
    }
}
