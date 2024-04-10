package aop.prototypes.redis.cache.controller;


import aop.prototypes.redis.cache.controller.dto.SaveMember;
import aop.prototypes.redis.cache.controller.dto.UpdateMember;
import aop.prototypes.redis.cache.domain.Product;
import aop.prototypes.redis.cache.entities.Member;
import aop.prototypes.redis.cache.entities.dto.MemberDto;
import aop.prototypes.redis.cache.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisController {

    private final ProductService productService;
    private final CacheManager cacheManager;

    @Autowired
    private final RedisTemplate<String, MemberDto> redisTemplateForMember;

    private static final String cache_key = "memberCache";

    @GetMapping("/redis/cache")
    public void cache() {
        cacheManager.getCache("products").clear();

        // 첫 번째 호출 - 메서드 실행 및 결과 캐시에 저장
        Product product1 = productService.getProductById("1");

        // 캐시에서 가져온 값이 실제 메서드 실행 결과와 일치하는지 확인
        log.info("product1.getName() : {}", product1.getName());

        // 두 번째 호출 - 캐시에서 결과 가져오므로 메서드 실행 없이 캐시 값 반환
        Product product2 = productService.getProductById("2");

        // 캐시 값이 반환되어야 함
        log.info("product2.getName() : {}", product2.getName());
    }

    @GetMapping("/redis/member/cache/{id}")
    public Member memberUseCacheManager(@PathVariable Long id) {
        cacheManager.getCache("member").clear();

        log.debug("idd : {}", id);
        return productService.getMemberByIdUsedCacheable(id);
    }

    @GetMapping("/redis/member/{id}")
    public MemberDto memberUseRedisTemplate(@PathVariable Long id) {
        if (redisTemplateForMember.opsForValue().get(cache_key) != null) {
            return redisTemplateForMember.opsForValue().get(cache_key);
        }

        log.info("product2.getName() : {}", redisTemplateForMember.opsForValue().get(cache_key));

        Member memberById = productService.getMemberByIdUseRedis(id);

        MemberDto memberDto = new MemberDto();
        memberDto.setName(memberById.getName());

        if (memberById != null) {
            redisTemplateForMember.opsForValue().set(cache_key, memberDto, Duration.ofSeconds(10L));
        }

        return memberDto;
    }

    @PostMapping("/redis/member")
    public void member(@RequestBody SaveMember saveMember) {
        productService.saveMember(saveMember.getName());
    }

    @PutMapping("/redis/member/{id}")
    public void member(@RequestBody UpdateMember updateMember, @PathVariable Long id) {
        productService.updateMember(id, updateMember.getName());
    }
}
