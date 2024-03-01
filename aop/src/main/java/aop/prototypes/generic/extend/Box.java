package aop.prototypes.generic.extend;


import aop.prototypes.generic.extend.subClass.Person;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class Box<T extends Person> {
    public T person;

    public T getPerson() {
        return person;
    }

    public void setPerson(T person) {
        this.person = person;
    }
}
