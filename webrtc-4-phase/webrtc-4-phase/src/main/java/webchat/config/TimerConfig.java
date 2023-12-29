package webchat.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "timer")
@Data
public class TimerConfig {
    private int checkKmsStateInterval;
}
