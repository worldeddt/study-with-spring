package aop.prototypes.generic.services;


import aop.prototypes.generic.interfaces.Obj;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class GenericService<T extends Obj>{

    private T obj;

    public GenericService(T obj) {
        this.obj = obj;
    }

    public int getValue() {
        return obj.getValue();
    }

    public <T> T methodGeneric(T value) {
        return value;
    }
}
