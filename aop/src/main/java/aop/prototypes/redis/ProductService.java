package aop.prototypes.redis;


import aop.prototypes.redis.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    @Cacheable("products")
    public Product getProductById(String id) {
        // 여기서는 예시로 단순히 ID로만 검색하는 로직을 가정
        // 실제로는 데이터베이스에서 데이터를 가져와야 합니다.

        log.info("dadf");
        return new Product(id, "Sample Product");
    }
}
