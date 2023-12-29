package webchat.timer.abstracts;

import jakarta.annotation.Resource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Instant;

public abstract class ADynamicScheduler {

    @Resource(name = "scheduler")
    private ThreadPoolTaskScheduler scheduler;

    public void stopScheduler() {
        scheduler.shutdown();
    }

    public void startScheduler(Object args) {
        scheduler.schedule(getRunnable(args), getTrigger());
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return this.scheduler;
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
        scheduler.schedule(getRunnable(String.valueOf(delay)), atTime.plusSeconds(delay));
    }

    public abstract void runner(Object args);

    public abstract Trigger getTrigger();
}
