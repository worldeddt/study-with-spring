package aop.prototypes.redis.cache.config;


import aop.prototypes.redis.cache.convertor.JsonSerializer;
import aop.prototypes.redis.cache.entities.Member;
import aop.prototypes.redis.cache.entities.dto.MemberDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MemberDto> redisTemplateForMember(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, MemberDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, Member> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Member> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Key Serializer 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Value Serializer 설정
        redisTemplate.setValueSerializer(new JsonSerializer<>(Member.class));

        return redisTemplate;
    }
}
