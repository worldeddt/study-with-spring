package aop.prototypes.redis;

import aop.prototypes.MainApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisExample {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
