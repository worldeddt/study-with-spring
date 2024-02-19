package aop.prototypes.thread.deadlock;

public class Resource {

    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public synchronized void method(Resource other) {
        Resource first;
        Resource second;

        synchronized (first) {
            System.out.println(Thread.currentThread().getName() + " is accessing resource: "+first.name);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " is trying to access resource: "+other.name);
        }

        other.method(this);
    }
}
