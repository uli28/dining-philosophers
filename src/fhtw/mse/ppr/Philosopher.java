package fhtw.mse.ppr;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ulrich Gram
 * @author Andreas Schranz
 */
public class Philosopher extends Thread {
    private static boolean INCREASE_CHANCE_FOR_DEADLOCK = false;
    private int id;
    private int thinkingTime;
    private int eatingTime;
    private long waitingTime = 0L;
    private long totalRuntime;
    private final Fork[] forks;
    private boolean isHungry = true;

    Philosopher(int id, int thinkingTime, int eatingTime, Fork[] forks) {
        this.id = id;
        this.thinkingTime = thinkingTime;
        this.eatingTime = eatingTime;
        this.forks = forks;
    }

    public void run() {
        long start = System.nanoTime();
        while (isHungry) {
            try {
                think();
                waitingTime += takeForks();
                eat();
                putForksBack();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long stop = System.nanoTime();
        totalRuntime = this.differenceToMillis(start, stop);
    }

    private void think() throws InterruptedException {
        int randomThinkingTime = ThreadLocalRandom.current().nextInt(0, this.thinkingTime + 1);
        System.out.println("{philosopher " + this.id + "} is thinking (" + randomThinkingTime + " ms)");
        Thread.sleep(randomThinkingTime);
    }

    private long takeForks() throws InterruptedException {
        System.out.println("{philosopher " + this.id + "} is waiting for first fork");
        long firstForkStart = System.nanoTime();
        takeFork(forks[0]);
        long firstForkStop = System.nanoTime();
        long firstForkWaitingTime = this.differenceToMillis(firstForkStart, firstForkStop);
        System.out.println("{philosopher " + this.id + "} took first fork (after waiting " + firstForkWaitingTime + " ms)");
        if (INCREASE_CHANCE_FOR_DEADLOCK) {
            Thread.sleep(500);
        }
        System.out.println("{philosopher " + this.id + "} is waiting for second fork");
        long secondForkStart = System.nanoTime();
        takeFork(forks[1]);
        long secondForkStop = System.nanoTime();
        long secondForkWaitingTime = this.differenceToMillis(secondForkStart, secondForkStop);
        System.out.println("{philosopher " + this.id + "} took first fork (after waiting " + secondForkWaitingTime + " ms)");
        return (firstForkWaitingTime + secondForkWaitingTime);
    }

    private void takeFork(final Fork fork) {
        fork.take();
    }

    private void eat() throws InterruptedException {
        int randomEatingTime = ThreadLocalRandom.current().nextInt(0, this.eatingTime + 1);
        System.out.println("{philosopher " + this.id + "} is eating (" + randomEatingTime + " ms)");
        Thread.sleep(randomEatingTime);
    }

    private synchronized void putForksBack() {
        System.out.println("{philosopher " + this.id + "} puts back first fork");
        //putForkBack(leftFork);
        forks[0].putBack();
        System.out.println("{philosopher " + this.id + "} puts back second fork");
        //putForkBack(rightFork);
        forks[1].putBack();
    }

    public void shutDown() {
        if (forks[0].getSemaphorePermits() > 0) { forks[0].putBack(); }
        if (forks[1].getSemaphorePermits() > 0) { forks[1].putBack(); }
        isHungry = false;
    }

    private long differenceToMillis(long start, long stop) {
        return (stop - start) / 1000000L;
    }

    public void printTimingResult() {
        System.out.println("{philosopher " + this.id + "} spent " + ((waitingTime * 100) / totalRuntime) + "% of total runtime waiting for forks");
    }
}

