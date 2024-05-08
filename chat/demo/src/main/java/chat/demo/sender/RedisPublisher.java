package chat.demo.sender;

import chat.demo.controller.dto.RedisPubDto;
import chat.demo.enums.RedisPubType;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPublisher {
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publishByUserId(String userId, Object object) {
        log.info("userId: {}, obj: {}", userId, object);
        redisTemplate.convertAndSend(channelTopic.getTopic(),
                    RedisPubDto.builder()
                            .redisPubType(RedisPubType.CALL)
                            .callId(userId)
                            .obj(object)
                            .build()
                );
    }

    public void publishByUserId(String userId, JsonObject object) {
        log.info("userId: {}, obj: {}", userId, object);
        redisTemplate.convertAndSend(channelTopic.getTopic(),object);
    }

}
