package webchat.timer.abstracts;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.Resource;
import java.time.Instant;

@Slf4j
public abstract class ADynamicScheduler {

    @Resource(name = "scheduleSC")
    private ThreadPoolTaskScheduler schedulerSC;

    public void stopScheduler() {
        schedulerSC.shutdown();
    }

    public void startScheduler(Object args) {
        log.info("schedulerSC : {}", schedulerSC);
        getScheduler().schedule(getRunnable(args), getTrigger());
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return this.schedulerSC;
    }

    public void setSchedulerSC(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.schedulerSC = threadPoolTaskScheduler;
    }

    private Runnable getRunnable(Object args) {
        return new Runnable() {
            @Override
            public void run() {
                runner(args);
            }
        };
    }

    public void excutionOnlyOne(long delay) {
        Instant atTime = Instant.now();
        schedulerSC.schedule(getRunnable(String.valueOf(delay)), atTime.plusSeconds(delay));
    }

    public abstract void runner(Object args);

    public abstract Trigger getTrigger();
}
