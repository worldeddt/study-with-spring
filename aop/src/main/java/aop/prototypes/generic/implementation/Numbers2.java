package aop.prototypes.generic.implementation;


import aop.prototypes.generic.interfaces.Obj;

public class Numbers2 implements Obj {

    private static final int number = 2;

    @Override
    public int getValue() {
        return number;
    }
}
