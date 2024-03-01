package aop.prototypes.generic.extend;

import aop.prototypes.generic.extend.subClass.Person;


public class Employee implements Person {

    private final Integer remain = 0;

    @Override
    public Integer remainBox() {
        return this.remain;
    }
}
