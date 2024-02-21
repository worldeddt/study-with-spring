package aop.prototypes.thread.spinlock;




public class SharedResource {
    private SpinLock spinLock = new SpinLock();
    private int counter = 0;

    private static final SharedResource sharedResource = new SharedResource();

    private SharedResource() {}

    public static SharedResource getInstance() {
        return sharedResource;
    }

    public void increment() {
        spinLock.lock();
        counter++;
        spinLock.unlock();
    }

    public int getCounter() {
        return counter;
    }
}
