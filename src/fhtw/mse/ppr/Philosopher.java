package fhtw.mse.ppr;

import java.util.concurrent.ThreadLocalRandom;

public class Philosopher {
    private int id;
    private int thinkingTime;
    private int eatingTime;
    private int[] forks = {-1, -1};    // id's of holden forks, -1 means that no fork is taken
    private boolean hasFinished = false;

    Philosopher(int id, int thinkingTime, int eatingTime) {
        this.id = id;
        this.thinkingTime = thinkingTime;
        this.eatingTime = eatingTime;
    }

    public void think() throws InterruptedException {
        System.out.println("{philosopher " + this.id + "} is thinking");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, this.thinkingTime + 1));
    }

    public void eat() throws InterruptedException {
        System.out.println("{philosopher " + this.id + "} is eating");
        Thread.sleep(ThreadLocalRandom.current().nextInt(0, this.eatingTime + 1));
    }

    public void takeFork(int index, int forkId) {
        this.forks[index] = forkId;
    }

    public int[] putBackForks() {
        int[] forksPutBack = new int[]{this.forks[0], this.forks[1]};
        this.forks[0] = -1;
        this.forks[1] = -1;
        this.hasFinished = true;
        return forksPutBack;
    }

    public boolean getHasFinished() {
        return this.hasFinished;
    }
}

