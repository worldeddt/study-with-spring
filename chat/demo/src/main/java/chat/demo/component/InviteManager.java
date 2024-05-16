package chat.demo.component;


import chat.demo.advice.CommonCode;
import chat.demo.advice.CommonException;
import chat.demo.enums.CallType;
import chat.demo.event.KeyExpiredPublishEvent;
import chat.demo.properties.ChatProperties;
import chat.demo.properties.TtlProperties;
import chat.demo.repository.CallEntityRepository;
import chat.demo.repository.InviteKeyEntityRepository;
import chat.demo.repository.SessionCacheRepository;
import chat.demo.repository.UserEntityRepository;
import chat.demo.repository.entity.Call;
import chat.demo.repository.entity.InviteKey;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InviteManager {
    private final InviteKeyEntityRepository inviteKeyEntityRepository;
    private final CallEntityRepository callEntityRepository;
    private final SessionCacheRepository sessionCacheRepository;
    private final UserEntityRepository userEntityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ChatProperties chatProperties;
    private final ChatTtlCache<String> ttlCache = new ChatTtlCache<>();
    private final TtlProperties ttlProperties;

    @Transactional(rollbackOn = CommonException.class)
    public String createInviteKey(String userId, CallType callType, String destinationId, Call call) {
        final String inviteKey = userId + "//" + UUID.randomUUID();

        String serverName = chatProperties.chatServer().serverName();
        final var inviteKeyCache = InviteKey.builder()
                .key(inviteKey)
                .userId(userId)
                .callType(callType)
                .server(serverName)
                .destnId(destinationId)
                .call(call)
                .isUsed(false)
                .build();
        inviteKeyEntityRepository.save(inviteKeyCache);

        // ttl 설정 및 expire 시 실행 로직
        ttlCache.put(inviteKey,
                (key) -> {
                    log.debug("inviteKey={} expired", key);
                    final var callId = call.getId();

                    final var selectedKey = inviteKeyEntityRepository.findById(key)
                            .orElseThrow(
                            () -> new CommonException(CommonCode.UNKNOWN_INVITE_KEY));

                    inviteKeyEntityRepository.save(
                            selectedKey.toBuilder().expiredDate(LocalDateTime.now()).build()
                    );

                    final var inviteKeyList =
                            inviteKeyEntityRepository.findAllByCallId(callId);

                    final var user = userEntityRepository.findById(userId).orElseThrow(
                            () -> new CommonException(CommonCode.NOT_AGENT));

                    final var selectedCall = callEntityRepository.findById(callId).orElseThrow(
                            () -> new CommonException(CommonCode.NO_CALL_FOUND));

                    final var sessionInfo = sessionCacheRepository.findById(userId).orElseThrow(
                            () -> new CommonException(CommonCode.NOT_FOUND_SESSION));

                    applicationEventPublisher.publishEvent(KeyExpiredPublishEvent.builder()
                            .callId(selectedCall.getId())
                            .inviteKey(inviteKey)
                            .principalName(sessionInfo.getPrincipalName())
                            .build());
                },
                ttlProperties.inviteKeyTtl(), ttlProperties.timeUnit());

        return inviteKey;
    }

    public synchronized Optional<InviteKey> useInviteKey(String inviteKey) {
        final var inviteKeyCache = inviteKeyEntityRepository.findById(inviteKey);
        inviteKeyCache.ifPresent(inviteKey1 -> {

            LocalDateTime expiredDate = inviteKey1.getExpiredDate();

            if (expiredDate != null) {
                if (Objects.equals(inviteKey1.getKey(), inviteKey)) {
                    inviteKey1.setExpiredDate(LocalDateTime.now());
                    inviteKey1.setIsUsed(TRUE);
                }
            }
        });

        ttlCache.remove(inviteKey);

        return inviteKeyCache;
    }
}
