package aop.prototypes.generic.controller;


import aop.prototypes.generic.implementation.Numbers;
import aop.prototypes.generic.implementation.Numbers2;
import aop.prototypes.generic.implementation.Numbers3;
import aop.prototypes.generic.interfaces.Obj;
import aop.prototypes.generic.services.GenericService;
import aop.prototypes.generic.services.GenericServiceClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
public class GenericController {

    @GetMapping(value = "/gene/origin")
    public void classOrigin() {
        GenericServiceClass<Numbers> genericServiceClass =
                new GenericServiceClass<>(new Numbers());

        log.info("generic service : {}", genericServiceClass.getObj().getValue());
    }

    @GetMapping(value = "/gene/class")
    public void classUseLimitTypeAndGetNumber() {

        GenericService<Numbers2> genericService = new GenericService<>(
                new Numbers2()
        );
//        genericService.print();

        log.info("object class: {}", genericService.getValue());
    }

    @GetMapping(value = "/gene/method")
    public void methodGet() {
        GenericService<Numbers> genericService = new GenericService<>(
                new Numbers()
        );

        log.info("method generic service : {}", genericService.methodGeneric(222));
    }

    @GetMapping(value = "/gene/wild")
    public void wildGet() {

        GenericService<Numbers3> genericService = new GenericService<>(
                new Numbers3()
        );

        processBox(genericService);
    }

    public void processBox(GenericService<?> genericService) {
        log.info("obj : {}",genericService.getValue());
    }
}
