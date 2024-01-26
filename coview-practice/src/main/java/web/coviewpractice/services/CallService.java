package web.coviewpractice.services;

import web.coviewpractice.payload.request.AcceptCallMessage;
import web.coviewpractice.payload.request.HelpCall;
import web.coviewpractice.payload.request.InboundCall;

import java.security.Principal;

public interface CallService {
    void handleOutboundCall(Principal principal) ;
    void handleHelpCall(Principal principal, HelpCall helpCall) ;

    void handleInboundCall(Principal principal, InboundCall inboundCall);

    void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage);

    void handleEndCall(Principal principal);
}
