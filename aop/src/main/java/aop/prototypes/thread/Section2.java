package aop.prototypes.thread;

import aop.prototypes.thread.services.DoWork;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequiredArgsConstructor
public class Section2 {
    @GetMapping(value = "/get/thread")
    private void task() throws InterruptedException {

        DoWork doWork = new DoWork();
        Thread thread1 = new Thread(doWork);

        // Thread 객체 생
        thread1.start();
        log.info("=====start====");
        TimeUnit.SECONDS.sleep(1);
        doWork.shouldStop = true;
        thread1.join();

        log.info("=====end====");
    }
}
