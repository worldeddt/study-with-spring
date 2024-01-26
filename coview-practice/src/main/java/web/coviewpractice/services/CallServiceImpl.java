package web.coviewpractice.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import web.coviewpractice.common.enums.AgentStatus;
import web.coviewpractice.common.enums.CallType;
import web.coviewpractice.common.enums.FermiErrorCode;
import web.coviewpractice.common.enums.NotificationType;
import web.coviewpractice.common.exception.FermiException;
import web.coviewpractice.components.SessionManager;
import web.coviewpractice.components.StompNotificationSender;
import web.coviewpractice.components.redis.AgentStatusManager;
import web.coviewpractice.components.redis.InviteManager;
import web.coviewpractice.payload.request.AcceptCallMessage;
import web.coviewpractice.payload.request.HelpCall;
import web.coviewpractice.payload.request.InboundCall;
import web.coviewpractice.payload.response.InviteMessage;

import java.security.Principal;


@Slf4j
@Service
@RequiredArgsConstructor
public class CallServiceImpl implements CallService {
    private final SessionManager sessionManager;

    private final AgentStatusManager agentStatusManager;
    private final InviteManager inviteManager;
//    private final InboundManager inboundManger;
//
//    private final AppRestClient appRestClient;
//    private final RedisPublisher redisPublisher;
    private final StompNotificationSender stompNotificationSender;

    @Override
    public void handleOutboundCall(Principal principal) {
        final var sessionInfo = sessionManager.findSessionInfo(principal);
        final var userId = sessionInfo.getUserId();

        if (!sessionInfo.isAgent())
            throw new FermiException(FermiErrorCode.ACCESS_DENIED);

        final var isLicenseExceeded = agentStatusManager.isLicenseExceeded(sessionInfo.getTenantId());

        if (isLicenseExceeded)
            throw new FermiException(FermiErrorCode.LICENCE_EXPIRED);

        final var isOwner = agentStatusManager.isOwner(userId);
        final var isCounseling = agentStatusManager.checkStatus(userId, AgentStatus.CI);

        if (isCounseling && !isOwner)
            throw new FermiException(FermiErrorCode.ACCESS_DENIED);

        if (!isCounseling) {
            log.info("start outbound");
            agentStatusManager.updateAgentStatus(userId, AgentStatus.RV);
            agentStatusManager.toBeOwner(userId, true);
        } else {
            log.info("add new outbound customer");
        }

        stompNotificationSender.sendCallNotification(principal,
                InviteMessage.builder()
                        .type(NotificationType.OUTBOUND_CALL)
                        .inviteKey(inviteManager.createInviteKey(userId, CallType.OUTBOUND)) // 취소 가능
                        .build()
        );
    }

    @Override
    public void handleHelpCall(Principal principal, HelpCall helpCall) {

    }

    @Override
    public void handleInboundCall(Principal principal, InboundCall inboundCall) {

    }

    @Override
    public void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage) {

    }

    @Override
    public void handleEndCall(Principal principal) {

    }
}
