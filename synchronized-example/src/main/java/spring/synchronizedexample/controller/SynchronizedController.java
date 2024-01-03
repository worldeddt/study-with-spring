package spring.synchronizedexample.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Component
@Slf4j
@RestController
public class SynchronizedController {
    private int count;
    private Counter counter;

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public void hi() {

        counter = new Counter();

        Thread thread1 = isThread(1);
        Thread thread2 = isThread(2);
        // 스레드 실행
        log.info("===thread1 start===");
        thread1.run();
        log.info("===thread2 start===");
        thread2.run();

        //모든 스레드의 실행을 기다림
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Final Counter : {}", counter.getCount());
    }

    private Thread isThread(int number) {
        return new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                log.info("count thread {} : {}",number, counter.getCount());
                counter.increment(number);
                counter.decrease(number);
            }
        });
    }

    @GetMapping("/count")
    public void sync(HttpSession session) {
        log.info("1 번째, id : {}", session.getId());
        count++;
        log.info("count : {}", count);
    }

}

@Slf4j
class Counter {
    private int count = 0;

    // synchronized 키워드를 사용하여 동시 접근 제어
    public synchronized void increment(int thread) {
        count++;
        log.info("count++ : {}, thread number : {}",count, thread);
    }

    public synchronized void decrease(int thread) {
        count--;
        log.info("count-- : {}, thread number : {}",count, thread);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count += count;
    }
}
