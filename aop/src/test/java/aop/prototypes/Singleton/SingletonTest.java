package aop.prototypes.Singleton;


import aop.prototypes.singleTon.services.SingletonService;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SingletonTest {


    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void test1() {
        SingletonService instance1 = SingletonService.getInstance();
        SingletonService instance2 = SingletonService.getInstance();

        System.out.println("instance 1"+instance1);
        System.out.println("instance 2"+instance2);

        assertThat(instance1).isSameAs(instance2);
    }

}
