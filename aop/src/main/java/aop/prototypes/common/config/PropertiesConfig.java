package aop.prototypes.common.config;

import aop.prototypes.MainApplication;
import aop.prototypes.oracle.OracleExample;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = {MainApplication.class, OracleExample.class})
public class PropertiesConfig {
    // @ConfigurationProperties 가 달린 객체들을 찾아서 yaml 값 매핑 후 빈으로 등록해주는 클래스
    // basePackageClasses 클래스가 있는 depth 부터 아래로 찾기 시작
}
