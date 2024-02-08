package aop.prototypes.kurentoMultiInstance.config;


import aop.prototypes.kurentoMultiInstance.enums.KurentoStatus;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.slf4j.event.Level;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
@EnableScheduling
public class KurentoConfig {
    private final KurentoProperties kurentoProperties;
    private final Map<String, KurentoClient> kurentoClientMap = new ConcurrentHashMap<>();
    private final Map<String, KurentoStatus> kurentoStatusMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        register();
        kmsDownCheck();
    }

    @Scheduled(fixedRate = 5000)
    private void kmsDownCheck() {
        log.info("======================= kms rewind =========================");

        kurentoStatusMap.forEach((String kmsUrl, KurentoStatus kurentoStatus) -> {
            if (kurentoStatus.getDescription().equals(KurentoStatus.CONNECTING.getDescription())) {
                log.info("kmsUrl reconnect : {}", kmsUrl);
                reconnect(kmsUrl);
            }

            if (kurentoStatus.getDescription().equals(KurentoStatus.CONNECTIONFAIL.getDescription())) {
                log.info("kmsUrl un register : {}", kmsUrl);
                reconnect(kmsUrl);
            }
        });
    }

    private synchronized void reconnect(String kmsUrl) {
        createKmsClient(kmsUrl);
    }


    private synchronized KurentoClient createKmsClient(String kmsUrl) {
        return new KurentoClientBuilder()
                .setKmsWsUri(kmsUrl)
                .setConnectionTimeout(20000L)
                .setTryReconnectingMaxTime(3L)
                .onConnected(() -> kurentoStatusMap.put(kmsUrl, KurentoStatus.CONNECTED))
                .onConnectionFailed(() -> kurentoStatusMap.put(kmsUrl, KurentoStatus.CONNECTIONFAIL))
                .onReconnecting(() -> kurentoStatusMap.put(kmsUrl, KurentoStatus.CONNECTING))
                .onReconnected(sameServer -> kurentoStatusMap.put(kmsUrl, KurentoStatus.CONNECTED))
                .onDisconnected(() -> kurentoStatusMap.put(kmsUrl, KurentoStatus.CONNECTIONFAIL))
                .connect();
    }

    private synchronized void register(String url) {
        kurentoClientMap.computeIfAbsent(url, parameter -> {
            kurentoStatusMap.put(parameter, KurentoStatus.CONNECTING);
            return createKmsClient(url);
        });
    }

    private synchronized  void register() {
        final var urls = kurentoProperties.urls();
        urls.forEach(this::register);
    }

    public synchronized void unregister(String url) {
        final var kurentoClient = kurentoClientMap.remove(url);
        kurentoClient.destroy();
    }

    public synchronized void unregister() {
        kurentoClientMap.keySet().forEach(this::unregister);
    }

}
