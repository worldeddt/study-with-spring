package chat.demo.event;


import chat.demo.properties.ChatProperties;
import chat.demo.repository.InviteKeyEntityRepository;
import chat.demo.repository.LicenseEntityRepository;
import chat.demo.repository.SessionCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1. agentStatus Entity 초기화
 * 2. inviteKey Entity 초기화
 * 3. session Entity(Redis) 초기화
 * 4. license Entity 초기화
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class AppEventHandler {

    private final InviteKeyEntityRepository inviteKeyEntityRepository;
    private final LicenseEntityRepository licenseEntityRepository;
    private final SessionCacheRepository sessionCacheRepository;

    private final ChatProperties callServerProperties;
}
