package media.ftf.domain;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import media.ftf.dto.response.KurentoDisconnectedEvent;
import media.ftf.dto.response.PipelineWrapper;
import media.ftf.enums.WebrtcStatus;
import media.ftf.properties.MediaProperties;
import media.ftf.utils.CommonUtils;
import media.ftf.utils.MediaCache;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.commons.exception.KurentoException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class Kurento {
    private final ConcurrentHashMap<String, KurentoClient> clients = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebrtcStatus> clientsStatus = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Float> usageRateMap = new ConcurrentHashMap<>();
    private final MediaProperties mediaProperties;
    private final MediaCache<String> ttlCache = new MediaCache<>(); // 재연결 이벤트
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostConstruct
    void init() {
        register();
    }

    public synchronized void register() {
        final var urls = mediaProperties.webrtc().kurentoUrlList();
        urls.forEach(this::register);
    }

    public synchronized void register(String url) {
        if (!clients.containsKey(url)) {
            final var kurentoClient = createKmsClient(url);

            if (kurentoClient != null) {
                clientsStatus.put(url, WebrtcStatus.LD);
                clients.put(url, kurentoClient);
            }
        }
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
                        changeStatus(kmsUrl, WebrtcStatus.CD);
                    })
                    .onReconnecting(() -> {  // Connected 이후 연결이 끊길 시 발생
                        log.info("@@@ onReconnecting {}", kmsUrl);
                        applicationEventPublisher.publishEvent(KurentoDisconnectedEvent.builder().kmsUrl(kmsUrl).build());
                        changeStatusFailed(kmsUrl, WebrtcStatus.LD);
                    })
                    .onConnectionFailed(() -> { // Connected 실패 시 발생
                        log.info("@@@ onConnectionFailed {}", kmsUrl);
                        changeStatusFailed(kmsUrl, WebrtcStatus.DC);
                    })
                    .onReconnected(server -> { // 발생을 하는가?
                        log.info("@@@ onReconnected {}", kmsUrl);
                        changeStatus(kmsUrl, WebrtcStatus.CD);
                    })
                    .onDisconnected(() -> { // 발생을 하는가?
                        log.info("onDisconnected {}", kmsUrl);
                        changeStatusFailed(kmsUrl, WebrtcStatus.DC);
                    })
                    .connect();
        } catch (KurentoException kurentoException) {
            log.error("connect error kurento : {}", kmsUrl);
            return null;
        }
    }

    public synchronized void changeStatusFailed(String kmsUrl, WebrtcStatus kurentoStatus) {
        clientsStatus.put(kmsUrl, kurentoStatus);
        kmsDownCheck(kmsUrl);
    }

    public synchronized void changeStatus(String kmsUrl, WebrtcStatus kurentoStatus) {
        clientsStatus.put(kmsUrl, kurentoStatus);
    }

    private synchronized void kmsDownCheck(String url) {
        if (ttlCache.get(url) == null) {
            ttlCache.put(url, this::reconnect, 5, TimeUnit.SECONDS);
        }
    }

    private synchronized void reconnect(String kmsUrl) {
        final var kurentoClient = createKmsClient(kmsUrl);

        if (kurentoClient != null) {
            clients.put(kmsUrl, kurentoClient);
        }
    }

    public synchronized void unregister(String url) {
        final var kurentoClient = clients.remove(url);
        kurentoClient.destroy();
    }

    // 프로세스 종료시 호출 필요
    public synchronized void unregister() {
        clients.keySet().forEach(this::unregister);
    }

    public synchronized PipelineWrapper createMediaPipeline() {

        if (clients.isEmpty()) return null;

        final var sortedMap = getKurentoClientSortedByMemoryAndCpuUsageRate();

        for (ConcurrentHashMap.Entry<String, Float> entry : sortedMap.entrySet()) {

            if (clientsStatus.get(entry.getKey()).getDescription().equals(WebrtcStatus.CD.getDescription())) {

                log.info("selected kms url : {}", entry.getKey());

                return PipelineWrapper.builder()
                        .mediaPipeline(clients.get(entry.getKey()).createMediaPipeline())
                        .url(entry.getKey())
                        .build();
            }
        }

        return null;
    }

    private TreeMap<String, Float> getKurentoClientSortedByMemoryAndCpuUsageRate() {

        usageRateMap.clear();

        for (String kmsUrl : clients.keySet()) {
            KurentoClient kurentoClient = clients.get(kmsUrl);

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
