package aop.prototypes.generic.extend;

import aop.prototypes.generic.extend.subClass.Person;

public class Employer implements Person {

    private final Integer remain = 1;

    @Override
    public Integer remainBox() {
        return this.remain;
    }
}