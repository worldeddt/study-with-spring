package webchat.webrtc3phase.service;


import com.cedarsoftware.util.io.JsonReader;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import webchat.webrtc3phase.domain.SessionIdOwner;
import webchat.webrtc3phase.dto.SessionInfo;
import webchat.webrtc3phase.enums.RedisProperties;
import webchat.webrtc3phase.infra.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService extends RedisService {
    private final String SESSION_KEY = RedisProperties.PF_SESSION + RedisProperties.INFO;
    private HashOperations<String, String, String> hashOps;
    private final Gson gson = new Gson();
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        this.hashOps = redisTemplate.opsForHash();
    }

    public void putSessionByUserId(String userId, SessionInfo session) {
        boolean result = hashOps.putIfAbsent(SESSION_KEY, userId, gson.toJson(session));
        log.info(result ? "레디스에 유저의 세션 정보 삽입 완료" :
                "레디스에 같은 아이디 세션 정보 이미 존재. 레디스에 추가하지 않음"
                        + "-> userId = {], session = {}", userId, session);
    }

    public void updateSessionByUserId(String userId, SessionInfo session) {
        SessionInfo sessionInfo = findSessionByUserId(userId);

        if (sessionInfo != null) {
            hashOps.put(SESSION_KEY, userId, gson.toJson(session));
            log.info("레디스에 유저의 세션 정보 업데이트 완료 -> userId = {}, session ={ }",
                    userId, session);
        } else {
            log.error("레디스에 유저의 세션 존재하지 않음. 업데이트 실패 -> userId={}", userId);
        }
    }

    public SessionInfo findSessionByUserId(String userId) {
        log.debug("레디스의 유저 세션 정보 가져옴 -> userId={}", userId);
        return (SessionInfo) JsonReader.jsonToJava(hashOps.get(SESSION_KEY, userId));
    }

    public void clearSessionInfoBySessionId(String sessionId) {
        log.info("clearSessionInfoBySessionId...{}", sessionId);
        // 210813
        final SessionInfo si = getSessionBySessionId(sessionId);

        userRepository.removeBySessionId(sessionId);

        // 220402, 소켓 오너를 삭제하기 전에 SessionInfo를 삭제해야 함. 순서 중요.
        if (si != null) removeSessionByUserId(si.getUserId());

        // 210805
        userRepository.removeSocketOwnerBySessionId(sessionId);
    }

    public SessionInfo getSessionBySessionId(String sessionId) {
        SessionIdOwner sessionIdOwner = userRepository.getSocketOwnerBySessionId(sessionId);

        return sessionIdOwner == null ? null : findSessionByUserId(sessionIdOwner.getUserId());
    }

    public long removeSessionByUserId(String userId) {
        log.debug("레디스의 유저세션 정보 삭제 -> userId={}", userId);
        return hashOps.delete(SESSION_KEY, userId);
    }
}
