package aop.prototypes.thread.visibilityExample;


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VisibilityExample {
    private static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        // 쓰레드 시작
        new Thread(() -> {
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true; // 변수 변경
            System.out.println("Flag value changed to true");
        }).start();

        // 메인 스레드에서 변수 읽기
        while (!flag) {
            System.out.println("Flag value is: " + flag);
            // flag가 true가 될 때까지 대기
        }
        System.out.println("Flag value is: " + flag);
    }
}
