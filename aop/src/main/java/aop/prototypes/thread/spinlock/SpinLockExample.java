package aop.prototypes.thread.spinlock;


import aop.prototypes.thread.spinlock.강의코드.UseSpin;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.StubFiles;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpinLockExample {

    public static void main(String[] args) {
//        SharedResource instance = SharedResource.getInstance();
//        instance.increment();

        UseSpin useSpin = new UseSpin();
        useSpin.startUp();
    }
}
