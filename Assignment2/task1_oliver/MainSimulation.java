import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class MainSimulation extends GlobalSimulation {

    private static final String DEFAULT_FILENAME = "assign_2_task_1.txt";

    public static void main(String[] args) throws IOException {
        confidenceInterval();
    }

    static double runSim(Config config, int throwAway, boolean write) throws IOException {
        reset();
        Event currentEvent;
        State state = new State(config);

        // state.printConfig();

        insertEvent(ARRIVAL, 0);
        insertEvent(MEASURE, 0 + state.T);

        while (!state.isDone) {
            currentEvent = eventList.fetchEvent();
            time = currentEvent.eventTime;
            state.treatEvent(currentEvent);
        }

        double mean = state.mean(throwAway);
        // System.out.println(String.format("%.2f", mean));

        if (write)
            writeResultsToFile(state.measurements(), DEFAULT_FILENAME);

        return mean;
    }

    /*
     * ALGORITHM
     * 1. Run the simulation to get mean
     * 2. Calculate mean of the means sampled
     * 3. Calculate stddev of the means sampled
     * 4. Calculate confidence (in this case based on σ mean )
     */
    static void confidenceInterval() throws IOException {
        Config config = new Config();
        config.setN(100)
                .setx(10)
                .setlambda(4.0)
                .setT(4)
                .setM(1000);
        int throwAway = 10;

        int iterations = 1000;
        List<Double> means = new LinkedList<>();

        for (int i = 0; i < iterations; i++) {
            means.add(runSim(config, throwAway, false));
        }

        double meanSum = means.stream()
                .reduce(0.0, (subtotal, element) -> subtotal + element);
        double mMean = meanSum / (double) iterations;

        // writeResultsToFile(means, "A2_t1_p4_CI.txt");

        double std = calcStandardDevEstimate(mMean, means);

        System.out.println();
        System.out.println("Std estimation: \t" + String.format("%.4f", std));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mMean) + " ± " + String.format("%.4f", 1.96 * std));

    }

    private static double calcStandardDevEstimate(double mMean, List<Double> means) {
        double sum = 0;
        int M = means.size();
        for (int i = 0; i < M; i++) {
            sum += Math.pow(means.get(i) - mMean, 2);
        }

        // NOTE! Using Bessel's correction, see wiki
        return Math.sqrt(sum / (double) (M - 1));
    }

    static <T> void writeResultsToFile(Iterable<T> ys, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);

        for (T y : ys) {
            fw.write(y + "\n");
        }

        fw.close();
    }

}