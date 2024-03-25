package aop.prototypes.rtcOutBound.as.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@MessageMapping("/call")
@RestController
public class CallSocketHandler {

    @MessageMapping("/requestCall")
    public void requestCall() {
        log.info("===== ===== request call ===== =====");
    }
}
