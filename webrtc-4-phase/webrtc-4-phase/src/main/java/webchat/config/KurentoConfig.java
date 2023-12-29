package webchat.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix="kms")
@Data
public class KurentoConfig
        implements IGwConfig {

    private List<String> urls = new ArrayList<String>();

    private String reconnDelayTimeMillis;
    private String recordPath;
    private String format;
    private boolean useAutoRecording;
    private boolean useEncordedRecording;
    private boolean useNewRecording;
    private boolean useComposite;
}
