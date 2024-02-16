package aop.prototypes.thread.deadlock;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeadLockExample {


    public static void main(String[] args) {
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
