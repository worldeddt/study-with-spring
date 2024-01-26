package web.coviewpractice.components.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.coviewpractice.common.data.CallKeyCache;
import web.coviewpractice.common.data.InviteKeyCache;
import web.coviewpractice.common.enums.CallType;
import web.coviewpractice.common.utils.FermiUtil;
import web.coviewpractice.repository.CallRedisRepository;
import web.coviewpractice.repository.InviteRedisRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class InviteManager {

    private final InviteRedisRepository inviteRedisRepository;
    private final CallRedisRepository callRedisRepository;

    public String createInviteKey(String userId, CallType callType) {
        return createInviteKey(userId, callType,null);
    }

    public String createInviteKey(String userId, CallType callType, String expiration) {
        // 유효시간 추가
        final String inviteKey = userId + "@" + UUID.randomUUID();

        final var inviteKeyCache =
                InviteKeyCache.builder().inviteKey(inviteKey).userId(userId).callType(callType).build();
        inviteRedisRepository.save(inviteKeyCache);

        if (callRedisRepository.findById(userId).isEmpty()) {
            matchCallId(userId, FermiUtil.generateRandomChain(8));
        }
        return inviteKey;
    }

    public synchronized Optional<InviteKeyCache> deleteInviteKey(String inviteKey) {
        final var inviteKeyCache = inviteRedisRepository.findById(inviteKey);
        inviteKeyCache.ifPresent(keyCache -> inviteRedisRepository.deleteById(keyCache.getInviteKey()));
        return inviteKeyCache;
    }

    public synchronized void deleteAllInviteKeyByUserId(String userId) {
        final var inviteKeyCacheList = inviteRedisRepository.findAllByUserId(userId);
        Iterable<String> inviteKeyList = inviteKeyCacheList.stream().map(InviteKeyCache::getInviteKey).toList();
        log.info("delete invite key with userId");
        inviteKeyList.forEach(System.out::println);
        inviteRedisRepository.deleteAllById(inviteKeyList);
    }

    public String getCallId(String userId) {
        return callRedisRepository.findById(userId).map(CallKeyCache::getCallId).orElse(null);
    }

    public void matchCallId(String userId, String callId) {
        callRedisRepository.save(CallKeyCache.builder().userId(userId).callId(callId).build());
    }
}
