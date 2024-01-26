package web.coviewpractice.main.ws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import web.coviewpractice.payload.request.RequestCallMessage;
import web.coviewpractice.services.CallService;
import web.coviewpractice.services.CallServiceImpl;


@Slf4j
@MessageMapping("/call")
@RequiredArgsConstructor
@RestController
public class CallHandler {
    private final CallServiceImpl callService;
    @MessageMapping("/requestCall")
    public void requestCallMessage(SimpMessageHeaderAccessor headerAccessor, RequestCallMessage requestCallMessage) {
        log.debug("{}", requestCallMessage);
        final var principal = headerAccessor.getUser();
        switch (requestCallMessage.getCallType()) {
            default -> log.info("default");
            case OUTBOUND -> callService.handleOutboundCall(principal);
            case HELP ->  callService.handleHelpCall(principal, requestCallMessage.getHelpCall());
            case INBOUND ->  callService.handleInboundCall(principal, requestCallMessage.getInboundCall());
        }
    }

    @MessageMapping("/acceptCall")
    public void acceptCallMessage(SimpMessageHeaderAccessor headerAccessor, RequestCallMessage requestCallMessage) {
        log.info("accept call");
    }
}
