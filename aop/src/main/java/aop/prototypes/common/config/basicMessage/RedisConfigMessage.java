package aop.prototypes.common.config.basicMessage;

import aop.prototypes.redis.cache.convertor.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Slf4j
@Configuration
public class RedisConfigMessage {
    @Bean
    public RedisTemplate<String, String> redisForMessage(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        log.info("redistemplate string string");
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JsonSerializer<>(String.class));

        return redisTemplate;
    }
}
