package fhtw.mse.ppr;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ulrich Gram
 * @author Andreas Schranz
 */
public class Philosopher extends Thread {
    private int id;
    private int thinkingTime;
    private int eatingTime;
    private final Fork leftFork;
    private final Fork rightFork;
    private boolean hasFinished = false;

    Philosopher(int id, int thinkingTime, int eatingTime, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.thinkingTime = thinkingTime;
        this.eatingTime = eatingTime;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void run() {
        while (!hasFinished) {
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
        System.out.println("{philosopher " + this.id + "} is thinking for: " + randomThinkingTime);
        Thread.sleep(randomThinkingTime);
    }

    private void takeForks() throws InterruptedException {
        System.out.println("{philosopher " + this.id + "} takes left fork");
        takeFork(leftFork);
        Thread.sleep(3000);

        System.out.println("{philosopher " + this.id + "} takes right fork");
        takeFork(rightFork);
    }

    private void takeFork(final Fork fork) throws InterruptedException {
        fork.take();
    }

    public void eat() throws InterruptedException {
        int randomEatingTime = ThreadLocalRandom.current().nextInt(0, this.eatingTime + 1);
        System.out.println("{philosopher " + this.id + "} is eating for: " + randomEatingTime);
        Thread.sleep(randomEatingTime);
    }

    private void putForksBack() {
        System.out.println("{philosopher " + this.id + "} put back left fork");
        putForkBack(leftFork);
        System.out.println("{philosopher " + this.id + "} put back right fork");
        putForkBack(rightFork);
    }

    private void putForkBack(Fork fork) {
        fork.putBack();
    }

    public void shutDown() {
        hasFinished = true;
    }
}

