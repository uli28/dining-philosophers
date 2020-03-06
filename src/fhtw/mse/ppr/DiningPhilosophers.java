package fhtw.mse.ppr;

import java.util.ArrayList;
import java.util.Scanner;

public class DiningPhilosophers {
    private static int n;
    private static int thinkingTime;
    private static int eatingTime;

    public static void main(String[] args) {
        final ArrayList<Philosopher> philosophers = new ArrayList<>();
        final ArrayList<Fork> forks = new ArrayList<>();
        final ArrayList<PhilosopherThread> philosopherThreads = new ArrayList<>();

        init(philosophers, forks);

        haveDinner(philosopherThreads, philosophers, forks);
    }

    private static void init(ArrayList<Philosopher> philosophers, ArrayList<Fork> forks) {
        readTableParameters();
        for (int i = 0; i < n; ++i) {
            philosophers.add(new Philosopher(i, thinkingTime, eatingTime));
            forks.add(new Fork(i));
        }
    }


    private static void haveDinner(ArrayList<PhilosopherThread> philosopherThreads, ArrayList<Philosopher> philosophers, ArrayList<Fork> forks) {
        for (int i = 0; i < n; ++i) {
            System.out.println("{philosopher " + i + "} starts dining");
            philosopherThreads.add(new PhilosopherThread(i, forks, philosophers, n));
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

    private static void readTableParameters() {
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
}
