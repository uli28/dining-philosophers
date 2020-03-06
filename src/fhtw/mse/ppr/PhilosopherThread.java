package fhtw.mse.ppr;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherThread extends Thread {

    private int philosopherId;
    private ReentrantLock mutex = new ReentrantLock();
    private final ArrayList<Fork> forks;
    private final ArrayList<Philosopher> philosophers;
    private int n;


    PhilosopherThread(int philosopherId, ArrayList<Fork> forks, ArrayList<Philosopher> philosophers, int n) {
        this.philosopherId = philosopherId;
        this.forks = forks;
        this.philosophers = philosophers;
        this.n = n;
    }

    public void run() {

        // thinking
        try {
            philosophers.get(this.philosopherId).think();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // taking fork on the left
        int forkId = this.philosopherId;
        boolean forkTaken = false;
        System.out.println("{philosopher " + this.philosopherId + "} is trying to take {fork " + forkId + "}");
        do {
            if (forks.get(forkId).getTakenBy() == -1) {
                try {
                    mutex.lock();
                    philosophers.get(this.philosopherId).takeFork(0, forkId);
                    forks.get(forkId).setTakenBy(this.philosopherId);
                    forkTaken = true;
                } finally {
                    mutex.unlock();
                }
            }
        } while (!forkTaken);

        // taking fork on the right
        forkId = (this.philosopherId + 1) % n;
        forkTaken = false;
        System.out.println("{philosopher " + this.philosopherId + "} is trying to take {fork " + forkId + "}");
        do {
            if (forks.get(forkId).getTakenBy() == -1) {
                try {
                    mutex.lock();
                    philosophers.get(this.philosopherId).takeFork(1, forkId);
                    forks.get(forkId).setTakenBy(this.philosopherId);
                    forkTaken = true;
                } finally {
                    mutex.unlock();
                }
            }
        } while (!forkTaken);

        // eating
        try {
            philosophers.get(this.philosopherId).eat();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // put back forks
        try {
            mutex.lock();
            int[] forksPutBack = philosophers.get(this.philosopherId).putBackForks();
            forks.get(forksPutBack[0]).setTakenBy(-1);
            forks.get(forksPutBack[1]).setTakenBy(-1);
        } finally {
            mutex.unlock();
        }
    }
}
