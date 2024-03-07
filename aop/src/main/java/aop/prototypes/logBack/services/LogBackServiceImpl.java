package aop.prototypes.logBack.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogBackServiceImpl implements LogBackService {

    @Override
    public void loggingMaster() {
        log.info("======== logging master system on info ========");
        log.error("======== logging master system on error ========");
        log.debug("======== logging master system on debug ========");
    }
}
