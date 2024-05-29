package media.ftf.application.interfaces;

import media.ftf.application.dto.request.TokenRequest;
import media.ftf.application.dto.response.MonitoringTokenResponse;
import media.ftf.application.dto.response.TokenResponse;
import media.ftf.module.SessionInfo;

import java.security.Principal;
import java.util.function.Consumer;

public interface SocketSessionService {
//    TokenResponse createSessionTicket(TokenRequest tokenRequest);

    MonitoringTokenResponse createMonitorTicket(SessionInfo sessionInfo);

    void register(Principal user, String userId, String roomId);

    void unregister(Principal user, Consumer<SessionInfo> callback);
}

