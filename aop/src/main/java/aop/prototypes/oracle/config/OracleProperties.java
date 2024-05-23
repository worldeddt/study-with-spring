package aop.prototypes.oracle.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@ConfigurationProperties("fermi.ssh")
public record OracleProperties(
        String dbUrl,
        String dbUsername,
        String dbPassword,
        String username,
        String password

) {
}
