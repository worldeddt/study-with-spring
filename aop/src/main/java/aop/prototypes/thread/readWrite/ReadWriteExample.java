package aop.prototypes.thread.readWrite;

import java.util.ArrayList;
import java.util.List;

public class ReadWriteExample {
    private volatile boolean shouldStop = false;

    private volatile int readCount = 0;
    private volatile int writeCount = 0;
    private volatile String message = String.valueOf(System.currentTimeMillis());


    public void startUp() {
        System.out.println("Process Start!");

        // Thread 오브젝트 만들기
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(this::write);
            thread.setName("Writer" + i);
            threads.add(thread);
        }

        for (int i = 0; i < 6; i++) {
            Thread thread = new Thread(this::read);
            thread.setName("Reader" + i);
            threads.add(thread);
        }

        // Thread 시작
        for (Thread thread : threads) {
            thread.start();
        }

        System.out.println("Press any key to stop...");
        //Console.ReadKey();

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

    /*
        주의: 이 예제는 Volatile.Read, Volatile.Write로 Reordering을 막고 있습니다.
        Interlocked.Increment, Interlocked.Decrement로 Atomicity를 보장하고 있지만 Reordering은 막지 않습니다.
        아래 코드는 문제 없이 돌아가지만 코드가 추가되면 문제가 발생할 여지가 많습니다.
        Lock을 쓰지 않고 멀티스레드 프로그래밍을 하는건 매우 위험합니다.
    */

    private void write() {
        while (!shouldStop) {
            if (readCount > 0 || writeCount > 0) {
                continue;
            }

            String request = String.valueOf(System.currentTimeMillis());
            writeCount++;
            try {
                message = request;
                System.out.println(Thread.currentThread().getName() + " : " + request);
            } finally {
                writeCount--;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void read() {
        while (!shouldStop) {
            if (writeCount > 0) {
                continue;
            }

            readCount++;

            try {
                String readMessage = message;
                System.out.println(Thread.currentThread().getName() + " : " + readMessage);
            } finally {
                readCount--;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
