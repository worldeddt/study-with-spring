package aop.prototypes.singleTon.services;

import aop.prototypes.singleTon.domain.Member;

public interface MemberService {

    void join(Member member);
    Member findMember(Long memberId);
}
