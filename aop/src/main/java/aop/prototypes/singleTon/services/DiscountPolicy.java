package aop.prototypes.singleTon.services;

import aop.prototypes.singleTon.domain.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
