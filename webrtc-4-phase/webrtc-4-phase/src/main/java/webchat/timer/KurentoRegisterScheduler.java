package webchat.timer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import webchat.config.ScheduleConfig;
import webchat.config.TimerConfig;
import webchat.service.KurentoService;
import webchat.timer.abstracts.ADynamicScheduler;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@Configuration
@RequiredArgsConstructor
public class KurentoRegisterScheduler extends ADynamicScheduler {

    private final TimerConfig timerConfig;
    private final KurentoService kurentoService;
    private final ScheduleConfig scheduleConfig;

    @PostConstruct
    public void startTimer() {
        log.info("=====================>startTimer()");
        kurentoService.init();
        this.setSchedulerSC(scheduleConfig.threadPoolTaskScheduler());
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
        log.info("========>     CheckInterval = {}", timerConfig.getCheckKmsStateInterval());
        return new PeriodicTrigger(Duration.of(10000,
                TimeUnit.MILLISECONDS.toChronoUnit()));
    }
}
