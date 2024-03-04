package aop.prototypes.singleTon.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingletonService {

    @Getter
    private static final SingletonService instance = new SingletonService();

    private SingletonService() {

    }

    private void logic() {
        log.info("싱글톤 객체 로직 호출");
    }
}
