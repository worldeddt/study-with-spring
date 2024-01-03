package webchat.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Data
@Configuration
@ConfigurationProperties(prefix = "timer")
public class TimerConfig {
    private int checkMsInterval;
    private int checkKmsStateInterval;
}
