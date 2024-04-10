package aop.prototypes.redis.cache.repository;


import aop.prototypes.redis.cache.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberEntityRepository extends JpaRepository<Member, Long> {
    @NotNull
    Optional<Member> findById(Long index);
}
