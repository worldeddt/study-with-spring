package chat.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;


@Configuration
@EnableCaching
public class SubscribeConfig {
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("topic1");
    }
}
