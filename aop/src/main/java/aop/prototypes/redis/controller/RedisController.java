package aop.prototypes.redis.controller;


import aop.prototypes.redis.ProductService;
import aop.prototypes.redis.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class RedisController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;
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
}
