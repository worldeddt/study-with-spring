package aop.prototypes.rtcOutBound.as.application;

import aop.prototypes.rtcOutBound.as.component.SessionManager;
import aop.prototypes.rtcOutBound.as.exception.CommonCode;
import aop.prototypes.rtcOutBound.as.exception.CommonException;
import aop.prototypes.rtcOutBound.as.model.payload.CallMessage;
import aop.prototypes.rtcOutBound.as.model.payload.OutboundCall;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallService {

    private final SessionManager sessionManager;
//    private final AgentEntityRepository  agentEntityRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void handleRequestCall(Principal principal, CallMessage callMessage) throws JsonProcessingException {

        if (callMessage.getCallType() == null) {
            throw new CommonException(CommonCode.BAD_REQUEST);
        }

        log.info("call type : {}", callMessage.getCallType());

        switch (callMessage.getCallType()) {
            default -> outBoundCall(principal, callMessage.getOutboundCall());
        }
    }

    private void outBoundCall(Principal principal, OutboundCall outboundCall) throws JsonProcessingException {

        log.info("out bound call : {}", objectMapper.writeValueAsString(outboundCall));

        final var sessionInfo = sessionManager.findSessionInfo(principal);
        final var userId = sessionInfo.getUserId();
        final var categoryId = outboundCall.getCategoryId();
        final var groupId = outboundCall.getGroupId();
        final var option = outboundCall.getOption();
        final var tenantId = sessionInfo.getTenantId();

        if (!sessionInfo.isAgent()) {
            log.warn("상담사가 아닌 유저");
            throw new CommonException(CommonCode.NOT_AGENT);
        }

//        final var agentEntity = agentEntityRepository.findById(userId).orElseThrow(() -> {
//            log.warn("알 수 없는 상담사");
//            return new CommonException(CommonCode.NOT_FOUND_AGENT);
//        });


    }
}
