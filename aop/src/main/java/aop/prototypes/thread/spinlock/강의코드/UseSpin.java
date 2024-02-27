package aop.prototypes.thread.spinlock.강의코드;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class UseSpin {
    // spin lock example

    static final int N = 100000;
    static Queue<Data> queue = new LinkedList<>();
    static Queue<Data> queue2 = new LinkedList<>();

    static Object lock = new Object();
    static Lock spinLock = new ReentrantLock();

    static class Data {
        String name;
        double number;
    }

    public void startUp() {
        useLock();
        queue.clear();
        useSpinLock();
    }

    private static void updateWithSpinLock(Data d, int i) {
        boolean lockTaken = false;
        try {
            lockTaken = spinLock.tryLock(1, TimeUnit.SECONDS);
            if (lockTaken)
                queue.add(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lockTaken)
                spinLock.unlock();
        }
    }

    private static void useSpinLock() {
        long startTime = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < N; i++) {
                updateWithSpinLock(new Data(), i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < N; i++) {
                updateWithSpinLock(new Data(), i);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("elapsed ms with spinlock: " + (endTime - startTime));
    }

    static void updateWithLock(Data d, int i) {
        synchronized (lock) {
            queue2.add(d);
        }
    }

    private static void useLock() {
        long startTime = System.currentTimeMillis();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < N; i++) {
                updateWithLock(new Data(), i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < N; i++) {
                updateWithLock(new Data(), i);
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("elapsed ms with lock: " + (endTime - startTime));
    }
}
