package aop.prototypes.rtcOutBound.as.handler;


import aop.prototypes.rtcOutBound.as.model.payload.CallMessage;
import aop.prototypes.rtcOutBound.as.application.CallService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@MessageMapping("/call")
@RestController
@RequiredArgsConstructor
public class CallSocketHandler {

    private final CallService callService;

    @MessageMapping("/requestCall")
    public void requestCall(SimpMessageHeaderAccessor headerAccessor, @Validated CallMessage callMessage)
            throws JsonProcessingException {
        final var principal = headerAccessor.getUser();
        log.info("principal : {}", principal);
        callService.handleRequestCall(principal, callMessage);
    }
}
