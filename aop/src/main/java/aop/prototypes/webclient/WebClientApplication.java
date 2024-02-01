package aop.prototypes.webclient;


import aop.prototypes.aop.AopApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }
}
