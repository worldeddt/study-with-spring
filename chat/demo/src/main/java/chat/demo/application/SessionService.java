package chat.demo.application;


import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.domain.SessionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class SessionService {



    private final SessionCacheRepository sessionCacheRepository;


    public void addSession(Principal user) {
        final var sessionInfo = SessionCache.builder()
                .principalName(user.getName()).build();


        sessionCacheRepository.save(sessionInfo);
    }
}
