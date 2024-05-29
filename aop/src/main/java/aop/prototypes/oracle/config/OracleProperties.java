package aop.prototypes.oracle.config;


import org.springframework.boot.context.properties.ConfigurationProperties;



public record OracleProperties(
        String dbUrl,
        String dbUsername,
        String dbPassword,
        String username,
        String password

) {
}
