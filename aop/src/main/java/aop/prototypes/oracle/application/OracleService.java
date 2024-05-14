package aop.prototypes.oracle.application;


import aop.prototypes.oracle.config.OracleConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.signature.qual.ClassGetName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class OracleService {

    private final String initOracleAccount;

    @GetMapping("/oracle")
    public void oracleDb() {
        log.info("oracle : {}", initOracleAccount);
    }
}
