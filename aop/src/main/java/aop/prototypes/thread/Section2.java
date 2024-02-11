package aop.prototypes.thread;

import aop.prototypes.MainApplication;
import kotlin.jvm.Volatile;
import org.springframework.boot.SpringApplication;

import java.io.Console;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Section2 {


    private static boolean shouldStop = false;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Section2.class, args);
task();

    }

    private static void task() throws InterruptedException {
        Thread thread1 = new Thread(Section2::doWork);
        thread1.start();
        System.out.printf("start");
        TimeUnit.SECONDS.sleep(3);
        shouldStop = true;
        thread1.join();
        System.out.printf("end");
    }

    private static void doWork() {

        boolean toggle = false;

        while (shouldStop == false) {
            toggle = !toggle;
        }
    }
}
