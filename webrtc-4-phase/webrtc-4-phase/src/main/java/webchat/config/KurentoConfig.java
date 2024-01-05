package webchat.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import webchat.infra.KmsClientRepository;
import webchat.service.KurentoService;
import webchat.service.RoomService;
import webchat.timer.KurentoRegisterScheduler;

import java.util.ArrayList;
import java.util.List;

//@Configuration
@Configuration
@Data
public class KurentoConfig {

    @Value("${kms.urls}")
    private List<String> urls;

    private String reconnDelayTimeMillis;
    private String recordPath;
    private String format;
    private boolean useAutoRecording;
    private boolean useEncordedRecording;
    private boolean useNewRecording;
    private boolean useComposite;

    @Bean
    public KurentoRegisterScheduler kurentoRegisterScheduler() {
        return new KurentoRegisterScheduler(new TimerConfig(), new KurentoService(
                new KmsClientRepository(), new KurentoConfig(), new RoomService()
        ), new ScheduleConfig());
    }
}
