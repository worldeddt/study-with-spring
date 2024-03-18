package aop.prototypes.redis.domain;

import lombok.AllArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor

public class Product  implements Serializable {
    private String id;
    private String name;
}
