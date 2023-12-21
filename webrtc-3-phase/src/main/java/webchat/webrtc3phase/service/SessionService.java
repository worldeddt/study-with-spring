package webchat.webrtc3phase.service;


import com.cedarsoftware.util.io.JsonReader;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import webchat.webrtc3phase.dto.SessionInfo;
import webchat.webrtc3phase.enums.RedisProperties;


@Slf4j
@Service
public class SessionService extends RedisService {
    private final String SESSION_KEY = RedisProperties.PF_SESSION + RedisProperties.INFO;
    private HashOperations<String,String,String> hashOps;
    private final Gson gson = new Gson();

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
                 userId, session );
        } else {
            log.error("레디스에 유저의 세션 존재하지 않음. 업데이트 실패 -> userId={}", userId);
        }
    }

    public SessionInfo findSessionByUserId(String userId) {
        log.debug("레디스의 유저세션 정보 가져옴 -> userId={}", userId);
        return (SessionInfo) JsonReader.jsonToJava(hashOps.get(SESSION_KEY, userId));
    }

}
