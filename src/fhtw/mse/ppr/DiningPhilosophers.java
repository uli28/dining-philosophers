package fhtw.mse.ppr;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static fhtw.mse.ppr.Subtask.DEADLOCK_PREVENTION_MODE;
import static fhtw.mse.ppr.Subtask.INITIAL_MODE;

/**
 * @author Ulrich Gram
 * @author Andreas Schranz
 */
public class DiningPhilosophers {
    private static Subtask mode = DEADLOCK_PREVENTION_MODE;

    private static int n;
    private static int thinkingTime;
    private static int eatingTime;

    public static void main(String[] args) {
        final ArrayList<Philosopher> philosophers = new ArrayList<>();
        final ArrayList<Fork> forks = new ArrayList<>();
        final ArrayList<Philosopher> philosopherThreads = new ArrayList<>();

        init(forks);
        haveDinner(philosophers, forks, philosopherThreads);
        printTimingResults(philosophers);
    }

    private static void init(ArrayList<Fork> forks) {
        readTableParameters();
        for (int i = 0; i < n; i++) {
            forks.add(new Fork());
        }
    }

    private static void readTableParameters() {
        n = readInput("Number of philosophers (>1): ", 2);
        thinkingTime = readInput("Maximal 'thinking time' (>0): ", 1);
        eatingTime = readInput("Maximal 'eating time' (>0): ", 1);
    }

    private static int readInput(String message, int minimumValue) throws InputMismatchException {
        int number = 0;
        boolean isInvalidInput = true;

        do {
            try {
                Scanner in = new Scanner(System.in);
                System.out.print(message);
                number = in.nextInt();
                isInvalidInput = false;
            } catch (Exception e) {
                System.out.println("invalid input");
            }
        } while (number < minimumValue && isInvalidInput);

        return number;
    }

    private static void haveDinner(ArrayList<Philosopher> philosophers, ArrayList<Fork> forks, ArrayList<Philosopher> philosopherThreads) {
        for (int i = 0; i < n; i++) {
            Fork[] philosopherForks = new Fork[2];
            philosopherForks[0] = forks.get(i);
            philosopherForks[1] = forks.get((i + 1) % forks.size());

            if (mode.equals(DEADLOCK_PREVENTION_MODE)) {
                if ((i % 2) != 0) {
                    philosopherForks[0] = forks.get((i + 1) % forks.size());
                    philosopherForks[1] = forks.get(i);
                }
            }

            Philosopher philosopher = new Philosopher(i, thinkingTime, eatingTime, philosopherForks);
            philosophers.add(philosopher);
            philosopherThreads.add(philosopher);
            philosopherThreads.get(i).start();
        }

        waitForKeyboardInput();

        System.out.println("stopping dinner");
        for (int i = 0; i < n; i++) {
            philosophers.get(i).shutDown();
            try {
                philosophers.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void waitForKeyboardInput() {
        Scanner s;
        do {
            s = new Scanner(System.in);
        } while (!s.nextLine().isEmpty());

    }

    private static void printTimingResults(ArrayList<Philosopher> philosophers) {
        System.out.println("\ntiming results:");
        for (int i = 0; i < n; i++) {
            philosophers.get(i).printTimingResult();
        }
    }
}
