package media.ftf.module;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.dto.response.KurentoDisconnectedEvent;
import media.ftf.dto.response.PipelineWrapper;
import media.ftf.enums.KurentoStatus;
import media.ftf.properties.MediaProperties;
import media.ftf.utils.CommonUtils;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.commons.exception.KurentoException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class KurentoManger {

    private final MediaProperties fermiProperties;
    private final ConcurrentHashMap<String, KurentoClient> map = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, KurentoStatus> statusMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Float> usageRateMap = new ConcurrentHashMap<>();
    private final CommonTtlCache<String> ttlCache = new CommonTtlCache<>(); // 재연결 이벤트
    private final ApplicationEventPublisher applicationEventPublisher;

    // 연결 끊길 시 룸 없애기 ()

    @PostConstruct
    public void init() {
        register();
    }

    public synchronized KurentoClient createKmsClient(String kmsUrl) {
        try {
            log.info("create KurentoClient - kmsUrl: {}", kmsUrl);
            return new KurentoClientBuilder()
                    .setConnectionTimeout(60L)
                    .setTryReconnectingMaxTime(10L)
                    .setKmsWsUri(kmsUrl)
                    .onConnected(() -> {
                        log.info("@@@ Connected {}", kmsUrl);
                        changeStatus(kmsUrl, KurentoStatus.CD);
                    })
                    .onReconnecting(() -> {  // Connected 이후 연결이 끊길 시 발생
                        log.info("@@@ onReconnecting {}", kmsUrl);
                        applicationEventPublisher.publishEvent(KurentoDisconnectedEvent.builder().kmsUrl(kmsUrl).build());
                        changeStatusFailed(kmsUrl, KurentoStatus.LD);
                    })
                    .onConnectionFailed(() -> { // Connected 실패 시 발생
                        log.info("@@@ onConnectionFailed {}", kmsUrl);
                        changeStatusFailed(kmsUrl, KurentoStatus.DC);
                    })
                    .onReconnected(server -> { // 발생을 하는가?
                        log.info("@@@ onReconnected {}", kmsUrl);
                        changeStatus(kmsUrl, KurentoStatus.CD);
                    })
                    .onDisconnected(() -> { // 발생을 하는가?
                        log.info("onDisconnected {}", kmsUrl);
                        changeStatusFailed(kmsUrl, KurentoStatus.DC);
                    })
                    .connect();
        } catch (KurentoException kurentoException) {
            log.error("connect error kurento : {}", kmsUrl);
            return null;
        }
    }

    public synchronized void changeStatusFailed(String kmsUrl, KurentoStatus kurentoStatus) {
        statusMap.put(kmsUrl, kurentoStatus);
        kmsDownCheck(kmsUrl);
    }

    public synchronized void changeStatus(String kmsUrl, KurentoStatus kurentoStatus) {
        statusMap.put(kmsUrl, kurentoStatus);
    }

    private synchronized void kmsDownCheck(String url) {
        if (ttlCache.get(url) == null) {
            ttlCache.put(url, this::reconnect, 5, TimeUnit.SECONDS);
        }
    }

    public synchronized void register(String url) {
        if (!map.containsKey(url)) {
            final var kurentoClient = createKmsClient(url);

            if (kurentoClient != null) {
                statusMap.put(url, KurentoStatus.LD);
                map.put(url, kurentoClient);
            }
        }
    }

    private synchronized void reconnect(String kmsUrl) {
        final var kurentoClient = createKmsClient(kmsUrl);

        if (kurentoClient != null) {
            map.put(kmsUrl, kurentoClient);
        }
    }

    public synchronized void unregister(String url) {
        final var kurentoClient = map.remove(url);
        kurentoClient.destroy();
    }


    public synchronized void register() {
        final var urls = fermiProperties.webrtc().kurentoUrlList();
        urls.forEach(this::register);
    }

    // 프로세스 종료시 호출 필요
    public synchronized void unregister() {
        map.keySet().forEach(this::unregister);
    }

    public synchronized PipelineWrapper createMediaPipeline() {

        if (map.isEmpty()) return null;

        final var sortedMap = getKurentoClientSortedByMemoryAndCpuUsageRate();

        for (ConcurrentHashMap.Entry<String, Float> entry : sortedMap.entrySet()) {

            if (statusMap.get(entry.getKey()).getDescription().equals(KurentoStatus.CD.getDescription())) {

                log.info("selected kms url : {}", entry.getKey());

                return PipelineWrapper.builder()
                        .mediaPipeline(map.get(entry.getKey()).createMediaPipeline())
                        .url(entry.getKey())
                        .build();
            }
        }

        return null;
    }

    private TreeMap<String, Float> getKurentoClientSortedByMemoryAndCpuUsageRate() {

        usageRateMap.clear();

        for (String kmsUrl : map.keySet()) {
            KurentoClient kurentoClient = map.get(kmsUrl);

            if (kurentoClient == null) continue;

            long memUsed = (kurentoClient.getServerManager().getUsedMemory() * 100) / 4045384;
            float cpuUsed = kurentoClient.getServerManager().getUsedCpu(1000);

            log.info("used total : {}", memUsed + cpuUsed);
            usageRateMap.put(kmsUrl, memUsed + cpuUsed);
        }

        TreeMap<String, Float> sortedMap = new TreeMap<>(new CommonUtils.ValueComparator(usageRateMap));
        sortedMap.putAll(usageRateMap);
        return sortedMap;
    }

}
