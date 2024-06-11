package aop.prototypes.kurentoMultiInstance.controller;


import aop.prototypes.media.common.config.KurentoConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class KurentoController {
    private final KurentoConfig kurentoConfig;

    @GetMapping(value = "/kurento")
    public void kurento() {

        log.info("---------------------------------------------------------------------------------------");

//        kurentoClientMap.forEach((String key, KurentoStatus kurentoStatus) -> {
//            log.atLevel(Level.INFO).log("key : {} | value : {}", key, kurentoStatus.getDescription());
//        });
    }
}
