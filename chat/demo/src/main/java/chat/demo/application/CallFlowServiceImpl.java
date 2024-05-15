package chat.demo.application;

import chat.demo.application.dto.AcceptCallMessage;
import chat.demo.application.interfaces.CallFlowService;
import chat.demo.component.InviteManager;
import chat.demo.repository.SessionCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Slf4j
@Service
@RequiredArgsConstructor
public class CallFlowServiceImpl implements CallFlowService {

    private final InviteManager inviteManager;
    private final SessionCacheRepository sessionCacheRepository;

    @Override
    public void handleAcceptCall(Principal principal, AcceptCallMessage acceptCallMessage) {
        log.info("session number : {}", principal.getName());

        final var sessionInfo = sessionCacheRepository.findByPrincipalName(principal.getName());

        if (acceptCallMessage.isAccept()) {
        }
    }
}
