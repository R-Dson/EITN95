import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class State extends GlobalSimulation {
    public int N;
    public int x;
    public double lambda;
    public int T;
    public int M;

    public int nbrOfCustomers = 0;

    private int[] ys;
    private int measurement_idx = 0;
    public boolean isDone = false;

    Random rand = new Random();

    State(Config config) {
        this.N = config.N;
        this.x = config.x;
        this.lambda = config.lambda;
        this.T = config.T;
        this.M = config.M;
        ys = new int[this.M];
    }

    public void treatEvent(Event x) {
        switch (x.eventType) {
            case MEASURE:
                measure();
                break;
            case ARRIVAL:
                arrival();
                break;
            case SERVED:
                served();
                break;
        }
    }

    private void arrival() {
        // a server is free
        if (nbrOfCustomers < N) {
            nbrOfCustomers++;
            insertEvent(SERVED, time + x);
        }

        insertEvent(ARRIVAL, time + expDistPdf(lambda));
    }

    private void served() {
        nbrOfCustomers--;
    }

    private void measure() {
        if (measurement_idx < M) {
            ys[measurement_idx] = nbrOfCustomers;
            measurement_idx++;
        }

        isDone = measurement_idx >= M;

        if (!isDone)
            insertEvent(MEASURE, time + T);
    }

    private double expDistPdf(double lambda) {
        return (-1.0) * Math.log(1 - rand.nextDouble()) / lambda;
    }

    double mean(int throwAway) {
        int start = throwAway;
        int meanSum = 0;
        for (int i = start; i < ys.length; i++) {
            meanSum += ys[i];
        }

        return 1.0 * (double) meanSum / ((double) ys.length - throwAway);
    }

    List<Integer> measurements() {
        return Arrays.stream(ys).boxed().collect(Collectors.toList());
    }

    public void printNbrInQueue() {
        System.out.println("-----");
        System.out.println(nbrOfCustomers);
        System.out.println("-----");
    }

    public void printConfig() {
        System.out.println("lambda\n\t" + lambda);
        System.out.println("Servers N\n\t" + N);
        System.out.println("Nbr of Measurements\n\t" + M);
    }
}
