package aop.prototypes.singleTon.repository;

import aop.prototypes.singleTon.domain.Member;

public interface MemberRepository {

    void save(Member member);

    Member findById(Long memberId);
}
