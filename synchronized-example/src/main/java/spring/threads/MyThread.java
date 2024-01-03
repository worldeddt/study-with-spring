package spring.threads;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyThread {

    public static void main(String[] args) {
        new MyThread().runTest();
    }
    
    @Slf4j
    static class MadThread implements Runnable {
        private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
        private final String name;

        public MadThread(String age) {
            this.name = age;
        }

        @Override
        public void run() {
            log.info("{} Started, ThreadLocal: {}", name, threadLocal.get());
            threadLocal.set(name);
            log.info("{} Finished, ThreadLocal: {}", name, threadLocal.get());
            threadLocal.remove();
        }
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void runTest() {
        for (int threadCount = 1; threadCount <= 5; threadCount++) {
            final String name = "thread-" + threadCount;
            log.info("start name : {}", name);
            final MadThread thread = new MadThread(name);
            executorService.execute(thread);
        }

        // 스레드 풀 종료
        executorService.shutdown();

        // 스레드 풀 종료 대기
        while (true) {
            try {
                if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                System.err.println("Error: " + e);
                executorService.shutdownNow();
            }
        }

        System.out.println("All threads are finished");
    }
}

