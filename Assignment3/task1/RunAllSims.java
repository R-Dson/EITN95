import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RunAllSims extends Global {

  static int[] ns = { 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000 };

  public static void main(String[] args) throws IOException {
    run();
  }

  private static void run() {
    ArrayList<Double> mmTput = new ArrayList<>();
    ArrayList<Double> mmPLoss = new ArrayList<>();
    Config config;
    double mean;
    int i = 0;
    for (int n : ns) {
      n = ns[ns.length - 1];
      System.out.println("----------------");
      System.err.println("n = " + n);

      config = getConfig(n);
      ArrayList<Double> means = runOneSim(config);
      mmTput.add(means.get(0));
      mmPLoss.add(means.get(1));

      System.out.println("Tput = " + means.get(0));
      System.out.println("PLos = " + means.get(1));
      System.out.println("----------------");
      break;
    }
  }

  public static ArrayList<Double> runOneSim(Config config) {
    reset();
    new SignalList();
    Signal actSignal;

    Gateway gateway = new Gateway(config.r, config.timeBetweenSamples, config.Tp);
    SignalList.SendSignal(MEASURE, gateway, time + config.timeBetweenSamples);

    // start sensors cycles
    for (int i = 0; i < config.n; i++) {
      int x = config.xs[i];
      int y = config.ys[i];
      Coord coord = new Coord(x, y);
      Sensor sensor = new Sensor(coord, gateway, config.ts);
      // stochastic, otherwise a lot of collisions immediately
      SignalList.SendSignal(SEND_MESSAGE, sensor, time + Statistics.expMean(config.ts));
    }

    int iter = 0;
    while (iter < config.nbrOfMeasurements) {
      actSignal = SignalList.FetchSignal();
      if (actSignal.signalType == MEASURE) {
        iter++;
      }
      time = actSignal.arrivalTime;
      actSignal.destination.TreatSignal(actSignal);
    }

    double meanSum = gateway.Tputs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    double meanTput = (1.0 / config.nbrOfMeasurements) * meanSum;

    meanSum = gateway.packageLossProbs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    double meanPloss = (1.0 / config.nbrOfMeasurements) * meanSum;
    ArrayList<Double> results = new ArrayList<>();
    results.add(meanTput);
    results.add(meanPloss);

    System.out.println("nbr of transmissions: " + gateway.nbrOfTransmissions);
    System.out.println("nbr of success: " + gateway.nbrOfSuccess);
    System.out.println("success ratio: " + (double) gateway.nbrOfSuccess / (double) gateway.nbrOfTransmissions);

    return results;
  }

  static Config getConfig(int n) {
    int ts = 4000;
    double r = 7000;
    double Tp = 1;
    double timeBetweenSamples = 2000;
    int nbrOfMeasurements = 100;

    Random slump = new Random();
    ArrayList<Integer> xlist = new ArrayList<>();
    ArrayList<Integer> ylist = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      int xSlump = slump.nextInt(10000);
      int ySlump = slump.nextInt(10000);

      // Can't overlap
      while (xlist.contains(xSlump) && ylist.contains(ySlump)) {
        xSlump = slump.nextInt(10000);
        ySlump = slump.nextInt(10000);
      }

      xlist.add(xSlump);
      ylist.add(ySlump);

    }

    Integer[] xs = xlist.toArray(new Integer[n]);
    Integer[] ys = ylist.toArray(new Integer[n]);

    return new Config(n, ts, Tp, r, timeBetweenSamples, nbrOfMeasurements, xs, ys);
  }
}