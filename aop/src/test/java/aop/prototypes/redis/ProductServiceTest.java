package aop.prototypes.redis;

import aop.prototypes.redis.domain.Product;
import aop.prototypes.redis.services.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;



@ExtendWith(SpringExtension.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void cache() {
        cacheManager.getCache("products").clear();

        // 첫 번째 호출 - 메서드 실행 및 결과 캐시에 저장
        Product product1 = productService.getProductById("1");

        // 캐시에서 가져온 값이 실제 메서드 실행 결과와 일치하는지 확인
        assertEquals("Sample Product", product1.getName());

        // 두 번째 호출 - 캐시에서 결과 가져오므로 메서드 실행 없이 캐시 값 반환
        Product product2 = productService.getProductById("2");

        // 캐시 값이 반환되어야 함
        assertEquals("Sample Product", product2.getName());
    }

}