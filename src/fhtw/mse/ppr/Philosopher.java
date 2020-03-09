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
    //private final Fork leftFork;
    //private final Fork rightFork;
    private final Fork[] forks;
    private boolean isHungry = true;

    //Philosopher(int id, int thinkingTime, int eatingTime, Fork leftFork, Fork rightFork, Fork[] forks) {
    Philosopher(int id, int thinkingTime, int eatingTime, Fork[] forks) {
        this.id = id;
        this.thinkingTime = thinkingTime;
        this.eatingTime = eatingTime;
        //this.leftFork = leftFork;
        //this.rightFork = rightFork;
        this.forks = forks;
    }

    public void run() {
        while (isHungry) {
            try {
                think();
                takeForks();
                eat();
                putForksBack();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void think() throws InterruptedException {
        int randomThinkingTime = ThreadLocalRandom.current().nextInt(0, this.thinkingTime + 1);
        System.out.println("{philosopher " + this.id + "} is thinking (" + randomThinkingTime + " ms)");
        Thread.sleep(randomThinkingTime);
    }

    private void takeForks() throws InterruptedException {
        System.out.println("{philosopher " + this.id + "} takes first fork");
        takeFork(forks[0]);
        if (INCREASE_CHANCE_FOR_DEADLOCK) {
            Thread.sleep(500);
        }
        System.out.println("{philosopher " + this.id + "} takes second fork");
        takeFork(forks[1]);
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

    private void putForkBack(Fork fork) {
        fork.putBack();
    }

    public void shutDown() {
        if (forks[0].getSemaphorePermits() > 0) { forks[0].putBack(); }
        if (forks[1].getSemaphorePermits() > 0) { forks[1].putBack(); }
        isHungry = false;
    }
}

