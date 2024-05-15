package chat.demo.application.interfaces;

import chat.demo.application.dto.AcceptCallMessage;

import java.security.Principal;

public interface CallFlowService {
    void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage);
}
