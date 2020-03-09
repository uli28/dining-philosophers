package fhtw.mse.ppr;

import java.util.concurrent.Semaphore;

/**
 * @author Ulrich Gram
 * @author Andreas Schranz
 */
public class Fork {

    private Semaphore lock = new Semaphore(1);

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

    public int getSemaphorePermits() {
        return lock.availablePermits();
    }
}
