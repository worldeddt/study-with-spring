package aop.prototypes.redis.cache.services;


import aop.prototypes.redis.cache.domain.Product;
import aop.prototypes.redis.cache.entities.Member;
import aop.prototypes.redis.cache.repository.MemberEntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final MemberEntityRepository memberEntityRepository;
    private final EntityManager entityManager;

    @Cacheable("products")
    public Product getProductById(String id) {
        // 여기서는 예시로 단순히 ID로만 검색하는 로직을 가정
        // 실제로는 데이터베이스에서 데이터를 가져와야 합니다.
        return new Product(id, "Sample Product");
    }

    @Cacheable("member")
    public Member getMemberByIdUsedCacheable(Long id) {
        Optional<Member> member = memberEntityRepository.findById(id);

        if (member.isPresent()) return member.get();

        return Member.builder()
                .name("")
                .id(0L)
                .build();
    }

    public Member getMemberByIdUseRedis(Long id) {
        return memberEntityRepository.findById(id).orElse(null);
    }

    public void saveMember(String name) {
        Member member = Member.builder()
                .name(name)
                .build();

        System.out.println("name 2: "+member.getName());
        memberEntityRepository.save(member);
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateMember(Long id, String name) {
        Optional<Member> member = memberEntityRepository.findById(id);

        if (member.isPresent()) {
            System.out.println("member 발견");

            member.get().setName(name);
            entityManager.flush();
        } else {
            System.out.println("member 미발견");
        }
    }
}
