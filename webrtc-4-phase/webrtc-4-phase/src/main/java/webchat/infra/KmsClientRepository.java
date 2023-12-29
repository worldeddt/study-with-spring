package webchat.infra;

import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.KurentoClientBuilder;
import org.kurento.commons.PropertiesManager;
import org.kurento.commons.PropertiesManager.PropertyHolder;
import org.springframework.stereotype.Repository;
import webchat.component.Utils;
import webchat.define.EMsStatus;
import webchat.model.KmsClientInfo;
import webchat.service.KurentoService;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Repository
public class KmsClientRepository {

    private static final ConcurrentHashMap<String, KmsClientInfo> kmsByUrl = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();

    public KmsClientInfo getByUrl(String kmsUrl) {
        return kmsByUrl.get(kmsUrl);
    }

    private static PropertyHolder propertyHolder = new PropertyHolder() {
        @Override
        public String getProperty(String property) {
            return properties.get(property);
        }
    };

    public void listRegisterdMs() {
        log.debug(".....List Registerd MS!!");
        int i = 0;
        for (final KmsClientInfo m : kmsByUrl.values()) {
            log.info(".........ConcurrentHashMap[{}]:{}", i, m.toString());
            i++;
        }
    }

    public KmsClientInfo removeByUrl(String kmsUrl) {
        final KmsClientInfo kmsInfo = kmsByUrl.get(kmsUrl);
        if (kmsInfo != null && kmsInfo.getKmsUrl() != null) {
            kmsByUrl.remove(kmsInfo.getKmsUrl());

            listRegisterdMs();
        }

        return kmsInfo;
    }

    public KurentoClient createKmsClient(String kmsUrl, KurentoService kmsService) {
        KurentoClient kurentoClient = new KurentoClientBuilder()
                .setKmsWsUri(kmsUrl)
                .setConnectionTimeout(20000L)
                .setTryReconnectingMaxTime(3L)
                .onConnectionFailed(() -> {
                    log.info("Kurento connectionFailed::{}:{}", kmsUrl, Utils.getDate());
                    kmsService.setKmsStatusOnly(kmsUrl, EMsStatus.DC);
                })
                .onConnected(() -> {
                    log.info("Kurento connected::{}:{}", kmsUrl, Utils.getDate());
                    kmsService.setKmsStatusOnly(kmsUrl, EMsStatus.CD);
                })
                .onDisconnected(() -> {
                    log.info("Kurento disconnected::{}:{}", kmsUrl, Utils.getDate());
                    kmsService.resetKms(kmsUrl);
                })
                .onReconnecting(() -> {
                    log.info("Kurento reconnecting...::{}:{}", kmsUrl, Utils.getDate());
                })
                .onReconnected(sameServer -> {
                    log.info("Kurento reconnected(sameServer-{})::{}:{}", sameServer, kmsUrl, Utils.getDate());
                    kmsService.setKmsStatusOnly(kmsUrl, EMsStatus.CD);
                })
                .connect();

        return kurentoClient;
    }

    public void register(String kmsUrl, String millis, KurentoService kmsService) {
        log.info("register::{}", kmsUrl);

        KmsClientInfo kms = new KmsClientInfo();
        kms.setRoomIds(new ArrayList<String>());
        kms.setKmsUrl(kmsUrl);
        // 211213
        properties.putIfAbsent("jsonRpcClientWebSocket.reconnectionDelay", millis);
        PropertiesManager.setPropertyHolder(propertyHolder);
        try {
            // 211213
            kms.setKurento(createKmsClient(kmsUrl, kmsService));
        } catch (Exception e) {
            log.warn("Kurento init error {}::{}", kmsUrl, e.getMessage());
            return;
        }

        kmsByUrl.put(kms.getKmsUrl(), kms);
    }
}
