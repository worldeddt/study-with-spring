package webchat.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import webchat.config.KurentoConfig;
import webchat.define.EMsStatus;
import webchat.infra.KmsClientRepository;
import webchat.model.KmsClientInfo;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class KurentoService {
    private List<String> kmsUrls;
    private String reconnDelayTime;    // millis
    private boolean useComposite;

    private KmsClientRepository kmsRepository;
    private KurentoConfig kurentoConfig;
    private RoomService roomService;

    public KurentoService(KmsClientRepository kmsClientRepository, KurentoConfig kurentoConfig, RoomService roomService) {
        this.kmsRepository = kmsClientRepository;
        this.kurentoConfig = kurentoConfig;
        this.roomService = roomService;
    }

    @PostConstruct
    public void init() {
        this.kmsUrls = new ArrayList<String>();
        this.kmsUrls.add("ws://localhost:8888/kurento");
        this.reconnDelayTime = kurentoConfig.getReconnDelayTimeMillis();
        this.useComposite = kurentoConfig.isUseComposite();
    }

    public synchronized void registKurentoClient() {
        for (String kmsUrl : kmsUrls) {
            log.info("kmsUrl : {}", kmsUrl);
            if (kmsRepository.getByUrl(kmsUrl) == null) {
                kmsRepository.register(kmsUrl, "10000", this);
            }
        }
        kmsRepository.listRegisterdMs();
    }

    public synchronized void setKmsStatusOnly(String kmsUrl, EMsStatus status) {
        KmsClientInfo kmsClient = kmsRepository.getByUrl(kmsUrl);

        log.info("Before....{}:{}", kmsUrl, kmsClient.getStatus());
        kmsClient.setStatus(status);
        log.info("After....{}:{}", kmsUrl, kmsClient.getStatus());
    }

    public void lock() {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
    }

    public synchronized void resetKms(String kmsUrl) {
        log.info("..................resetKms({})", kmsUrl);
        // 211213
        KmsClientInfo kmsClient = kmsRepository.getByUrl(kmsUrl);
        kmsRepository.removeByUrl(kmsUrl);
//        clearKmsInfo(kmsUrl, kmsClient);
        destroyKms(kmsUrl, kmsClient);
    }

    // 211213
    public void destroyKms(String kmsUrl, KmsClientInfo kmsClient) {
        log.info("destroyKms:{}", kmsUrl);
        if (kmsClient.getKurento() != null) {
            kmsClient.getKurento().destroy();
        }
        // 211213
        kmsClient = null;
    }

}
