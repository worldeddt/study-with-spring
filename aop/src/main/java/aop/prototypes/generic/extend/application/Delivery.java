package aop.prototypes.generic.extend.application;

import aop.prototypes.generic.extend.Box;
import aop.prototypes.generic.extend.Employee;
import aop.prototypes.generic.extend.Employer;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Delivery {

    public static void main(String[] args) {
        send();
    }
    static void send() {
        Box<Employee> numberBox1 = new Box<>(new Employee());
        Box<Employer> numberBox2 = new Box<>(new Employer());
        System.out.printf("number1 : "+numberBox1.getPerson().remainBox());
        System.out.printf("number2 : "+numberBox2.getPerson().remainBox());
    }
}
