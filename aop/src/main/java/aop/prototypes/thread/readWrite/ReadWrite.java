package aop.prototypes.thread.readWrite;


import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class ReadWrite {
    public static void main(String[] args) {
        ReadWriteExample readWriteExample = new ReadWriteExample();
        readWriteExample.startUp();
    }
}
