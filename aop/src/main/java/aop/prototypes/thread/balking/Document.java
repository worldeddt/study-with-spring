package aop.prototypes.thread.balking;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Document {
    private boolean changed = false;

    public synchronized void change() {
        this.changed = true;
        log.info("Document is changed");
        notify();
    }

    public synchronized void save() {
        while(!changed) {
            try
            {
                log.info("save is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("error : {}", e.getMessage());
                return;
            }
        }

        log.info("Saving the document....");
        this.changed = false;
    }
}
