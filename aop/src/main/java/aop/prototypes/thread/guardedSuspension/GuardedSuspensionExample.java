package aop.prototypes.thread.guardedSuspension;

import java.util.LinkedList;
import java.util.Queue;

public class GuardedSuspensionExample {
    private volatile boolean shouldStop = false;
    private final Queue<String> requests = new LinkedList<>();
    private final Queue<String> queueRequests = new LinkedList<>();
    public void startUp() {
        System.out.println("Process Start!");

        // 스레드 오브젝트 만들기
        Thread thread1 = new Thread(this::doClient);
        thread1.setName("Client");
        Thread thread2 = new Thread(this::doServer);
        thread2.setName("Server");

        // 스레드 시작
        thread1.start();
        thread2.start();

        System.out.println("Press any key to stop...");
        try {
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

        shouldStop = true;

        // thread2가 Wait 상태일 때, 아래 코드가 없으면 종료되지 않음
        synchronized (requests) {
            /**
             #참고
             Monitor.PulseAll 대기열에 있는 쓰레드를 모두 깨움.
             Monitor.Wait 실행을 중지하고 대기열로 감.
             C#의 Monitor.PulseAll을 Java의 notifyAll로 대체하였습니다.
             C#의 Monitor.Wait을 Java의 wait로 대체하였습니다.
             */
            requests.notifyAll();
        }

        // 스레드 종료 대기
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All done!");
    }

    private void doClient() {
        while (!shouldStop) {
            synchronized (requests) {
                String request = String.valueOf(System.currentTimeMillis());
                requests.offer(request);
                requests.notifyAll(); // 일해라 핫산!!
                System.out.println(Thread.currentThread().getName() + " : " + request);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void doServer() {
        while (!shouldStop) {
            synchronized (requests) {
                if (!requests.isEmpty()) {
                    String request = requests.poll();
                    System.out.println(Thread.currentThread().getName() + " : " + request);
                } else {
                    System.out.println(Thread.currentThread().getName() + " : Wait");
                    try {
                        requests.wait(); // 일 없으니까 쉴래요
                        System.out.println(Thread.currentThread().getName() + " : Awake");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new GuardedSuspensionExample().startUp();
    }
}
