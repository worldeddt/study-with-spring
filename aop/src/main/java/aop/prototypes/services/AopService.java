package aop.prototypes.services;


import aop.prototypes.controller.dto.User;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.util.Base64;


@Slf4j
@Aspect
@Service
public class AopService {

    @Pointcut("execution(* aop.prototypes.controller.AopController.*.*(..))")
    public void initAopService() {
        log.info("initAopService() ");
    }

    // 메서드가 실행되는 시점 이전
    @Before("initAopService()")
    public void before(JoinPoint joinPoint) {
        log.info("init aop before start");

        Signature signature = joinPoint.getSignature();
        MethodSignature signature1 = (MethodSignature) signature;

        log.info("{} 메서드 실행", signature1.getMethod().getName());
    }

    // 메서드가 실행되는 시점 이후
    @After("initAopService()")
    public void after() {
        log.info("init aop after start");
    }

    //@AfterReturning 어노테이션의 returning 값과 afterReturn 매개변수 obj의 이름이 같아야 함
    @AfterReturning(value = "initAopService()", returning = "object")
    public void afterReturn(JoinPoint joinPoint, Object object) {
        // Object : initAopService 메소드가 호출 실행된 곳의 반환값
        log.info("obj : {}", object);

        if ( object instanceof User) {
            User user = User.class.cast(object);
            String email = user.getEmail();

            String base64Email = Base64.getEncoder().encodeToString(email.getBytes());
            user.setEmail(base64Email);
        }
    }
}
