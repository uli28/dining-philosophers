package fhtw.mse.ppr;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class Philosophers {

    static class Philosopher
    {
        private int id;
        private int thinkingTime;
        private int eatingTime;
        private int[] forks = { -1, -1 };    // id's of holden forks, -1 means that no fork is taken
        private boolean hasFinished = false;

        public Philosopher(int id, int thinkingTime, int eatingTime) {
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

        public void takeFork(int index, int forkId)
        {
            this.forks[index] = forkId;
        }

        public int[] putBackForks()
        {
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

    static class Fork
    {
        private int id;
        private int takenBy;    // id of philosopher holding fork, -1 means that no fork is taken

        public Fork(int id) {
            this.id = id;
            this.takenBy = -1;
        }

        public int getTakenBy() {
            return this.takenBy;
        }

        public void setTakenBy(int philosopherId) {
            int oldPhilosopherId = this.takenBy;
            this.takenBy = philosopherId;
            if (philosopherId == -1) {
                System.out.println("{philosopher " + oldPhilosopherId + "} put back {fork " + this.id + "}");
            }
            else {
                System.out.println("{philosopher " + philosopherId + "} took {fork " + this.id + "}");
            }
        }
    }

    static class PhilosopherThread extends Thread {

        private int philosopherId;
        private ReentrantLock mutex = new ReentrantLock();

        public PhilosopherThread(int philosopherId) {
            this.philosopherId = philosopherId;
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
                    }
                    finally {
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
                    }
                    finally {
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
            }
            finally {
                mutex.unlock();
            }
        }
    }



    static int n;
    static int thinkingTime;
    static int eatingTime;
    static ArrayList<Philosopher> philosophers = new ArrayList<>();
    static ArrayList<Fork> forks = new ArrayList<>();
    static ArrayList<PhilosopherThread> philosopherThreads = new ArrayList<>();

    static void init() {
        readTableParameters();
        for (int i = 0; i < n; ++i)
        {
            philosophers.add(new Philosopher(i, thinkingTime, eatingTime));
            forks.add(new Fork(i));
        }
    }

    static void readTableParameters() {
        Scanner in = new Scanner(System.in);
        int number;

        do {
            System.out.print("Number of philosophers (>1): ");
            number = in.nextInt();
        } while (number < 2);
        n = number;

        do {
            System.out.print("Maximal 'thinking time' (>0): ");
            number = in.nextInt();
        } while (number < 1);
        thinkingTime = number;

        do {
            System.out.print("Maximal 'eating time (>0):");
            number = in.nextInt();
        } while (number < 1);
        eatingTime = number;
    }

    static void haveDinner() {
        for (int i = 0; i < n; ++i) {
            System.out.println("{philosopher " + i + "} starts dining");
            philosopherThreads.add(new PhilosopherThread(i));
            philosopherThreads.get(i).start();
        }

        ArrayList<Integer> philosophersHavingFinished = new ArrayList<>();
        do {
            for (int i = 0; i < n; ++i) {
                if ((!philosophersHavingFinished.contains(i)) && (philosophers.get(i).getHasFinished())) {
                    try {
                        philosopherThreads.get(i).join();
                        System.out.println("{philosopher " + i + "} stops dining");
                        philosophersHavingFinished.add(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } while (philosophersHavingFinished.size() < n);
    }

    public static void main(String[] args) {
        init();

        haveDinner();
    }
}
