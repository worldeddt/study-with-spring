package aop.prototypes.kurentoMultiInstance.controller;


import aop.prototypes.kurentoMultiInstance.config.KurentoConfig;
import aop.prototypes.kurentoMultiInstance.enums.KurentoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.slf4j.event.Level;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
public class KurentoController {
    private final KurentoConfig kurentoConfig;

    @GetMapping(value = "/kurento")
    public void kurento() {

        Map<String, KurentoStatus> kurentoClientMap = kurentoConfig.getKurentoStatusMap();
        log.info("---------------------------------------------------------------------------------------");

        kurentoClientMap.forEach((String key, KurentoStatus kurentoStatus) -> {
            log.atLevel(Level.INFO).log("key : {} | value : {}", key, kurentoStatus.getDescription());
        });
    }
}
