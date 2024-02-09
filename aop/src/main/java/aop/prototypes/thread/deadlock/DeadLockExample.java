package aop.prototypes.thread.deadlock;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeadLockExample {


    public static void main(String[] args) {

        Resource resource1 = new Resource("Resource 1");
        Resource resource2 = new Resource("Resource 2");

        Thread thread3 = new Thread(() -> {
            resource1.method(resource2);
        });

        Thread thread4 = new Thread(() -> {
            resource2.method(resource1);
        });

        thread3.start();
        thread4.start();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


}
