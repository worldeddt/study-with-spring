package aop.prototypes.Singleton;

import aop.prototypes.singleTon.config.AppConfig;
import aop.prototypes.singleTon.repository.MemberRepository;
import aop.prototypes.singleTon.services.MemberServiceImpl;
import aop.prototypes.singleTon.services.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

public class ConfigurationTest {

    @Test
    void configurationTest() {
        AnnotationConfigApplicationContext ac
                = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService",
                MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService",
                OrderServiceImpl.class);

        MemberRepository memberRepository = ac.getBean("memberRepository",
                MemberRepository.class);

//모두 같은 인스턴스를 참고하고 있다.
        System.out.println("memberService -> memberRepository = " +
                memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository  = " +
                orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);
    }
}
