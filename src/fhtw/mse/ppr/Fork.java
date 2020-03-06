package fhtw.mse.ppr;

public class Fork {
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
