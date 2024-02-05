package aop.prototypes.generic.implementation;


import aop.prototypes.generic.interfaces.Obj;
import org.springframework.stereotype.Service;

@Service
public class Numbers implements Obj {

    public static final int number = 1;

    @Override
    public int getValue() {
        return number;
    }
}
