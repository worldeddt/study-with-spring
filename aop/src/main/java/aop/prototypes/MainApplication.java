package aop.prototypes;

import aop.prototypes.common.config.KurentoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


//@SpringBootApplication
@EnableConfigurationProperties(KurentoProperties.class)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
