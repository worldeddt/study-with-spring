package aop.prototypes.thread.balking;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BalkingExample {

    public static void main(String[] args) {

        Document document = new Document();

        Thread changeThread = new Thread(() -> {
            document.change();
           try {
               Thread.sleep(1000);
           }catch (InterruptedException e) {
               log.error("e changethread : {}", e.getStackTrace());
           }
           document.change();
        });

        Thread saveThread = new Thread(() -> {
            document.save();
        });

        //balking 패턴을 구현하기 위하여 change를 넣는다 .
        changeThread.start();
        saveThread.start();
    }
}

