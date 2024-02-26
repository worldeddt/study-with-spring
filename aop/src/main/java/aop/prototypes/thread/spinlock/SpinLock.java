package aop.prototypes.thread.spinlock;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpinLock {
    private AtomicBoolean locked = new AtomicBoolean(false);

    public void lock() {
        while (!locked.compareAndSet(false, true)) {
            // locked가 false일 때만 true로 설정하여 락을 획득합니다.
            // compareAndSet() 메서드는 원래 값과 새 값이 같은지 비교하고, 같으면 새 값으로 설정합니다.
            // 만약 다른 스레드가 이미 락을 획득한 상태라면 반복해서 재시도합니다.
            // 이는 Busy-waiting 방식의 스핀락입니다.
        }
    }

    public void unlock() {
        locked.set(false); // 락을 해제합니다.
    }
}
