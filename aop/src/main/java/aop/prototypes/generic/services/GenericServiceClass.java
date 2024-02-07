package aop.prototypes.generic.services;


import aop.prototypes.generic.interfaces.Obj;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class GenericServiceClass<T>{

    private T obj;

    public T getObj() {
        return obj;
    }
}
