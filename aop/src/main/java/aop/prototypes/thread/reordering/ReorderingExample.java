package aop.prototypes.thread.reordering;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReorderingExample {
    //volatile 설정을 하게 되면 메인 메모리에 값을 즉시 반환하게 된다.
    // 그러한 연유로 무한 루프에 빠지지 않게 된다 뭐 그런 설명이다.
    private static volatile boolean ready;
    private static volatile int number;

    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready) {
                System.out.printf("thread ing...");
                Thread.yield(); // 다른 스레드에게 실행 양보
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        ready = true;
        number = 42;

    }
}