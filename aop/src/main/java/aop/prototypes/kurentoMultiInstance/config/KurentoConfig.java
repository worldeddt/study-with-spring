package aop.prototypes.kurentoMultiInstance.config;


import aop.prototypes.kurentoMultiInstance.enums.KurentoStatus;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.client.MediaPipeline;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
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
    private final ConcurrentHashMap<String, Float> usageRateMap = new ConcurrentHashMap<>();

//    @PostConstruct
    public void init() {
        register();
        kmsDownCheck();
    }

    @Scheduled(fixedRate = 5000)
    private void kmsDownCheck() {
        log.info("======================= kms rewind =========================");

        kurentoStatusMap.forEach((String kmsUrl, KurentoStatus kurentoStatus) -> {
            if (kurentoStatus.getDescription().equals(KurentoStatus.CONNECTIONFAIL.getDescription()) ||
                    kurentoStatus.getDescription().equals(KurentoStatus.CONNECTING.getDescription())
            ) {
                log.info("kmsUrl un register : {}", kmsUrl);
                reconnect(kmsUrl);
            }
        });
    }

    private synchronized void reconnect(String kmsUrl) {
        createKmsClient(kmsUrl);
    }

    public synchronized MediaPipeline createMediaPipeline() {

        usageRateMap.clear();

        for (String kmsUrl : kurentoClientMap.keySet()) {
            KurentoClient kurentoClient = kurentoClientMap.get(kmsUrl);
            long memUsed = (kurentoClient.getServerManager().getUsedMemory() * 100) / 4045384;
            float cpuUsed = kurentoClient.getServerManager().getUsedCpu(1000);

            usageRateMap.put(kmsUrl, memUsed + cpuUsed);
        }

        TreeMap<String, Float> sortedMap = new TreeMap<>(new ValueComparator(usageRateMap));
        sortedMap.putAll(usageRateMap);


        for (ConcurrentHashMap.Entry<String, Float> entry : sortedMap.entrySet()) {
            log.info("key : {} | value : {}", entry.getKey(), entry.getValue());
        }

        return null;
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

    class ValueComparator implements Comparator<String> {
        ConcurrentHashMap<String, Float> map;

        public ValueComparator(ConcurrentHashMap<String, Float> map) {
            this.map = map;
        }

        @Override
        public int compare(String key1, String key2) {
            if (map.get(key1) > map.get(key2)) {
                return 1;
            } else if (map.get(key1) < map.get(key2)) {
                return -1;
            } else {
                // 값이 같으면 키를 기준으로 정렬
                return key1.compareTo(key2);
            }
        }
    }

}
