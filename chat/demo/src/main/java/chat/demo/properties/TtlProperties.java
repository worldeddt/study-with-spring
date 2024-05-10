package chat.demo.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(value = "chat.ttl")
public record TtlProperties(
        TimeUnit timeUnit,
        long inviteKeyTtl
) {
}
