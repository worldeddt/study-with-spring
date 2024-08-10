package aop.prototypes.media;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@SpringBootApplication
@EnableConfigurationProperties
public class MediaExample {


    public static void main(String[] args) {
        SpringApplication.run(MediaExample.class, args);
    }
}
