package aop.prototypes.media.application;

import aop.prototypes.media.common.config.KurentoConfig;
import aop.prototypes.media.common.properties.KurentoProperties;
import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;


@Component
@RequiredArgsConstructor
public class Scheduler implements CommandLineRunner {

    private final KurentoProperties kurentoProperties;

    private final KurentoConfig kurentoConfig;

    private final LinkedHashMap<String, KurentoClient> kurentoMap;


    @Override
    public void run(String... args) throws Exception {
        final var urls = kurentoProperties.urls();

        urls.forEach(this::register);
    }

    public synchronized void register(String url) {
        if (!kurentoMap.containsKey(url)) {
            final var kurentoClient = kurentoConfig.createKmsClient(url);

            if (kurentoClient != null) {
                kurentoMap.put(url, kurentoClient);
            }
        }
    }
}
