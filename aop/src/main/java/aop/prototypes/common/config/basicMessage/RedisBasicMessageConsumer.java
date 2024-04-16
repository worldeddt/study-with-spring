package aop.prototypes.common.config.basicMessage;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisBasicMessageConsumer implements MessageListener {

    @Qualifier("redisForMessage")
    private final RedisTemplate<String, String> redisForMessage;
    private final String room = "room1";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();

        redisForMessage.convertAndSend(room, body);
    }
}