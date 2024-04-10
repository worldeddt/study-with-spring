package aop.prototypes.redis.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@AllArgsConstructor
@Getter
public class Product  implements Serializable {
    private String id;
    private String name;
}
