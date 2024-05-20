package chat.demo.application;


import chat.demo.model.SessionInfo;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.entity.SessionCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionCacheRepository sessionCacheRepository;

    public synchronized void addSession(Principal user, String userId) {

        log.info("user session : {}/ user id : {}", user.getName(), userId.split("/")[0]);

        final var sessionCache =
                SessionCache.builder()
                        .userId(userId.split("/")[0])
                        .isHost(Objects.equals(userId.split("/")[1], "1"))
                        .principalName(user.getName())
                        .build();

        sessionCacheRepository.save(sessionCache);
    }

    public synchronized void removeSession(Principal user) {
        final var sessionInfo = sessionCacheRepository.findByPrincipalName(user.getName());

        if (sessionInfo != null) {
            final var userId = sessionInfo.getUserId();
            sessionCacheRepository.deleteById(userId);
        }
    }
}
