package aop.prototypes.singleTon.config;

import aop.prototypes.singleTon.repository.MemberRepository;
import aop.prototypes.singleTon.repository.MemoryMemberRepository;
import aop.prototypes.singleTon.services.MemberService;
import aop.prototypes.singleTon.services.MemberServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;

public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }


    @Bean
    private static MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }


}
