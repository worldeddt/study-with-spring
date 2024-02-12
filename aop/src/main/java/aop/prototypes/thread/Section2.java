package aop.prototypes.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Section2 {
    public boolean shouldStop = false;
    @GetMapping(value = "/get/thread")
    private void task() throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doWork();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Thread 객체 생
        thread1.start();
        log.info("=====start====");
        TimeUnit.SECONDS.sleep(1);
        shouldStop = true;
        thread1.join();

        log.info("=====end====");
    }

    private void doWork() throws InterruptedException {
        boolean toggle = false;

        while(!shouldStop) {
            toggle = !toggle;
            Thread.sleep(1000);
        }
    }
}
