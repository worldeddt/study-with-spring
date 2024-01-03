package webchat.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import webchat.infra.KmsClientRepository;
import webchat.service.KurentoService;
import webchat.service.RoomService;
import webchat.timer.KurentoRegisterScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;


@Slf4j
@EnableScheduling
@Configuration
public class ScheduleConfig extends ThreadPoolTaskScheduler {
    private static final long serialVersionUID = 7478248959031852404L;

    @Bean(name = "scheduler")
    TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("scheduler-");
        log.info("scheduling start : ");

        scheduler.initialize();
        return scheduler;
    }

    @Bean(name="kush")
    public KurentoRegisterScheduler kurentoRegisterScheduler() {
        return new KurentoRegisterScheduler(
                new TimerConfig(), new KurentoService(
                new KmsClientRepository(), new KurentoConfig(),
                new RoomService())
        );
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
        return super.scheduleAtFixedRate(task, startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
        return super.scheduleAtFixedRate(task, period);
    }
}
