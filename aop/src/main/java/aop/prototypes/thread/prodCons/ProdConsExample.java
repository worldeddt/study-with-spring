package aop.prototypes.thread.prodCons;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Array;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.*;

@Slf4j
public class ProdConsExample {
    private volatile boolean shouldStop = false;
    private Queue<String> requests = new ConcurrentLinkedDeque<>();
    public void startUp() {
        System.out.println("Process Start!");
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(this::producer);
            thread.setName("Producer" + i);
            threads.add(thread);
        }

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(this::consumer);
            thread.setName("Consumer" + i);
            threads.add(thread);
        }

        // Thread 시작
        for (Thread thread : threads) {
            thread.start();
        }

        System.out.println("Press any key to stop...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shouldStop = true;

        // Thread 종료 대기
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All done!");
    }

    private void producer() {
        while(!shouldStop) {
            String request = String.valueOf(System.currentTimeMillis());
            requests.offer(request);
            log.info(Thread.currentThread().getName() + " : " + request);
        }
    }

    private void consumer() {
        while(!shouldStop) {
            String request = requests.poll();
            if (request != null) {
                log.info(Thread.currentThread().getName() + " : " + request);
            }
        }
    }
}
