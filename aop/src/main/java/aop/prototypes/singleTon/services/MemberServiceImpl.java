package aop.prototypes.singleTon.services;

import aop.prototypes.singleTon.domain.Member;
import aop.prototypes.singleTon.repository.MemberRepository;
import aop.prototypes.singleTon.repository.MemoryMemberRepository;

public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository  = new MemoryMemberRepository();

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
