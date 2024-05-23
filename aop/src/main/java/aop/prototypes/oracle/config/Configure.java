package aop.prototypes.oracle.config;


import aop.prototypes.MainApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationPropertiesScan(basePackageClasses = MainApplication.class)
public class Configure {
}
