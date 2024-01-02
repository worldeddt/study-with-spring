package webchat.timer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import webchat.config.TimerConfig;
import webchat.service.KurentoService;
import webchat.timer.abstracts.ADynamicScheduler;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class KurentoRegisterScheduler extends ADynamicScheduler {

    private final TimerConfig timerConfig;
    private final KurentoService kurentoService;

    public void startTimer() {

        log.info("=====================>startTimer()");
        startScheduler("kurento register timer");
    }

    public void stopTimer() {
        stopScheduler();
    }

    @Override
    public void runner(Object args) {
        log.info("running kurento scheduler");
        kurentoService.registKurentoClient();
    }

    @Override
    public Trigger getTrigger() {
        return new PeriodicTrigger(timerConfig.getCheckKmsStateInterval(),
                TimeUnit.MILLISECONDS
        );
    }
}
