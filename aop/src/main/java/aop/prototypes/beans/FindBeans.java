package aop.prototypes.beans;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FindBeans {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext
                context = new AnnotationConfigApplicationContext();
        context.scan("aop");
        context.refresh();
        String[] beansNames = context.getBeanDefinitionNames();

        for (String beansName : beansNames) {
            System.out.println("beansName : "+beansNames);
        }
    }
}
