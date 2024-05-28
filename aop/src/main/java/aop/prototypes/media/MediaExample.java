package aop.prototypes.media;


import aop.prototypes.common.TemplateExample;
import aop.prototypes.common.config.KurentoConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MediaExample {


    public static void main(String[] args) {
        SpringApplication.run(MediaExample.class, args);
    }
}
