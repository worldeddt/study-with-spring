package chat.demo.application.interfaces;

import chat.demo.application.dto.AcceptCallMessage;
import chat.demo.application.dto.RequestCallMessage;

import java.security.Principal;

public interface CallFlowService {
    void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage);

    void handleRequestCall(Principal principal, RequestCallMessage requestCallMessage);
}
