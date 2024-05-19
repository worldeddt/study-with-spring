package chat.demo.application;


import chat.demo.model.SessionInfo;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.entity.SessionCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.Principal;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionCacheRepository sessionCacheRepository;

    public synchronized void addSession(Principal user, String userId) {

        log.debug("user session : {}/ user id : {}", user.getName(), userId);

        final var sessionCache =
                SessionCache.builder()
                        .userId(userId)
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
