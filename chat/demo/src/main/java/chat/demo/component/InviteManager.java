package chat.demo.component;


import chat.demo.advice.CommonException;
import chat.demo.enums.CallType;
import chat.demo.properties.ChatProperties;
import chat.demo.properties.TtlProperties;
import chat.demo.repository.InviteKeyEntityRepository;
import chat.demo.repository.entity.Call;
import chat.demo.repository.entity.InviteKey;
import chat.demo.repository.entity.SessionCache;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InviteManager {

    private final ChatProperties chatProperties;
    private final ChatTtlCache<String> ttlCache = new ChatTtlCache<>();
    private final TtlProperties ttlProperties;
    private final InviteKeyEntityRepository inviteKeyEntityRepository;

    @Transactional(rollbackOn = CommonException.class)
    public String createInviteKey(String userId, CallType callType, String destinationId, Call call) {
        final String inviteKey = userId+"//"+UUID.randomUUID().toString();

        String serverName = chatProperties.chatServer().serverName();

        final var inviteKeyCache = InviteKey.builder()
                .inviteKey(inviteKey)
                .hostId(userId)
                .callType(callType)
                .serverName(serverName)
                .call(call)
                .destnId(destinationId)
                .isUsed(false)
                .build();
        inviteKeyEntityRepository.save(inviteKeyCache);


        ttlCache.put(inviteKey,
                (key) -> {

                },
                ttlProperties.inviteKeyTtl(), ttlProperties.timeUnit()
        );

        return inviteKey;
    }
}
