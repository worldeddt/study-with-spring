package media.ftf.application;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.advice.CommonException;
import media.ftf.application.dto.response.MonitoringTokenResponse;
import media.ftf.application.interfaces.SocketSessionService;
import media.ftf.enums.CommonCode;
import media.ftf.module.SessionInfo;
import media.ftf.module.SessionManager;
import media.ftf.properties.MediaProperties;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocketSessionServiceImpl implements SocketSessionService {

    private final ObjectMapper objectMapper;

    private final MediaProperties mediaProperties;
    private final SessionManager sessionManager;

    @Override
    public MonitoringTokenResponse createMonitorTicket(SessionInfo sessionInfo) {
        return null;
    }

    @Override
    public synchronized void register(Principal user, String authorization) {
        log.info("authorization {} ", authorization);

        final var sessionInfo = SessionInfo.builder()
                .principal(user)
                .userId(authorization)
                .build();

        log.info("{}", sessionInfo);

        if (sessionManager.hasSession(sessionInfo)) {
            log.warn("duplicate login {}", sessionInfo);
            throw new CommonException(CommonCode.DUPLICATED_LOGIN);
        }

        sessionManager.registerSession(sessionInfo);
    }

    @Override
    public synchronized void unregister(Principal user, Consumer<SessionInfo> callback) {
        final var sessionInfo = sessionManager.removeSession(user);
//        callback.accept(sessionInfo);
    }
}
