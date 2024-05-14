package aop.prototypes.oracle.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OracleConfig {

    @Bean
    public String initOracleAccount() {
        String oracleDb = System.getenv("ORACLE_DB");

        return oracleDb == null ? "PANTHEON" : oracleDb;
    }
}
