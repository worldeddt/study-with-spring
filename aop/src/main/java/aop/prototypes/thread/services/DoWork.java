package aop.prototypes.thread.services;

public class DoWork implements  Runnable {
    public boolean shouldStop = false;
    @Override
    public void run() {

        boolean toggle = false;

        while(!shouldStop) {
            toggle = !toggle;
        }
    }
}
