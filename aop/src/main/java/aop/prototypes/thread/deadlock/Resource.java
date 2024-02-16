package aop.prototypes.thread.deadlock;

public class Resource {

    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public synchronized void method(Resource other) {
        System.out.println(Thread.currentThread().getName() + " is accessing resource: "+name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " is trying to access resource: "+other.name);
        other.method(this);
    }
}
