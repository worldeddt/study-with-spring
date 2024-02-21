package aop.prototypes.thread.spinlock;


import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.StubFiles;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpinLockExample {

    public static void main(String[] args) {
        SharedResource instance = SharedResource.getInstance();
        log.info("start");
        instance.increment();
        log.info("counter : {}", instance.getCounter());
        log.info("end");
    }
}
