package webchat.timer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import webchat.config.TimerConfig;
import webchat.service.KurentoService;
import webchat.timer.abstracts.ADynamicScheduler;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KurentoRegisterScheduler extends ADynamicScheduler {

    private final TimerConfig timerConfig;
    private final KurentoService kurentoService;

    @Bean
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
        log.info("========>     CheckInterval = {}", timerConfig.getCheckMsInterval());
        return new PeriodicTrigger(Duration.of(timerConfig.getCheckKmsStateInterval(),
                TimeUnit.MILLISECONDS.toChronoUnit()));
    }
}
