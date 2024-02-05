package aop.prototypes.generic.implementation;


import aop.prototypes.generic.interfaces.Obj;

public class Numbers3 implements Obj {

    private static final int number = 3;

    @Override
    public int getValue() {
        return number;
    }
}
