package aop.prototypes.thread.deadlock;

public class Resource {

    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public synchronized void method() {

    }
}
