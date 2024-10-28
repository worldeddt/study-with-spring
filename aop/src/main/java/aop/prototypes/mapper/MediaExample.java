package aop.prototypes.mapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@SpringBootApplication
@EnableConfigurationProperties
public class MediaExample {


    public static void main(String[] args) {
        SpringApplication.run(MediaExample.class, args);
    }
}
