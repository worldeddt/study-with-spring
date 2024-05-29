package aop.prototypes.media.common.config;


import aop.prototypes.common.domain.TtlCache;
import aop.prototypes.kurentoMultiInstance.enums.KurentoStatus;
import aop.prototypes.media.common.properties.KurentoProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.commons.exception.KurentoException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
@EnableScheduling
public class KurentoConfig {

    private final KurentoProperties kurentoProperties;
    private final ConcurrentHashMap<String, KurentoClient> map = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, KurentoStatus> statusMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Float> usageRateMap = new ConcurrentHashMap<>();
    private final TtlCache<String> ttlCache = new TtlCache<>(); // 재연결 이벤트

    @PostConstruct
    public void init() {
        register();
    }

    public void changeKurentoStatus(String url, String kurentoStatus) {

        KurentoStatus kurentoStatus1 = KurentoStatus.valueOf(kurentoStatus);

        statusMap.put(url, kurentoStatus1);

        log.info("status : {}", statusMap.get(url));
    }

    public KurentoClient createKmsClient(String kmsUrl) {
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
//                        applicationEventPublisher.publishEvent(KurentoDisconnectedEvent.builder().kmsUrl(kmsUrl).build());
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

    public synchronized void changeStatus(String kmsUrl, KurentoStatus kurentoStatus) {
        statusMap.put(kmsUrl, kurentoStatus);
    }

    public synchronized void register(String url) {
        if (!map.containsKey(url)) {
            final var kurentoClient = createKmsClient(url);

            if (kurentoClient != null) {
                statusMap.put(url, KurentoStatus.CD);
                map.put(url, kurentoClient);
            }
        }
    }

    public synchronized void register() {
        final var urls = kurentoProperties.urls();
        urls.forEach(this::register);
    }

    private synchronized void changeStatusFailed(String url, KurentoStatus kurentoStatus) {
        statusMap.put(url, kurentoStatus);
        kmsDownCheck(url);
    }

    private synchronized void kmsDownCheck(String url) {
        if (ttlCache.get(url) == null) {
            ttlCache.put(url, this::reconnect, 5, TimeUnit.SECONDS);
        }
    }

    private synchronized void reconnect(String url) {
        KurentoClient kurentoClient = createKmsClient(url);
        if (kurentoClient != null) {
            map.put(url, kurentoClient);
        }
    }
}
