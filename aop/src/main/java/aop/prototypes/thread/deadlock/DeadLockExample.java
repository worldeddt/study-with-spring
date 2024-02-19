package aop.prototypes.thread.deadlock;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeadLockExample {


    public static void main(String[] args) {

        Resource resource1 = new Resource("Resource 1");
        Resource resource2 = new Resource("Resource 2");

        Thread thread0 = new Thread(() -> {
            resource1.method(resource2);
        });

        Thread thread1 = new Thread(() -> {
            resource2.method(resource1);
        });

        thread0.start();
        thread1.start();
    }
}
