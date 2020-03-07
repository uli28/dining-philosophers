package fhtw.mse.ppr;

import java.util.concurrent.Semaphore;

/**
 * @author Ulrich Gram
 * @author Andreas Schranz
 */
public class Fork {

    public Semaphore lock = new Semaphore(1);

    public void take() {

        try {
            lock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void putBack() {
        lock.release();
    }

    boolean isFree() {
        return lock.availablePermits() > 0;
    }
}
