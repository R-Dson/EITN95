import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainSimulation extends Global {

  public static void main(String[] args) throws IOException {
    runSim();
  }

  public static void runSim() throws IOException {
    new SignalList();
    Signal actSignal;

    // MyFileReader f = new MyFileReader(configFileName);
    // f.readFile();
    // Config config = f.getConfig();
    Config config = getConfig();

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

    double meanSum = gateway.zs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    double mean = (1.0 / config.nbrOfMeasurements) * meanSum;

    System.out.println("nbr of transmissions: " + gateway.nbrOfTransmissions);
    System.out.println("nbr of success: " + gateway.nbrOfSuccess);
    System.out.println(mean);
  }

  static Config getConfig() {
    int n = 1000;
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

  // -------------------------------------------------------
  // -------------------------------------------------------
  // -------------------------------------------------------

  static void testGatewayCollisions() {
    Config config = getConfig();
    Gateway gateway = new Gateway(config.r, config.timeBetweenSamples, config.Tp);
    Coord c1 = new Coord(1, 1);
    Coord c2 = new Coord(1, 2);
    System.out.println("Distance: " + c1.distance(c2));
    System.out.println("Should be true due to overlap: " + (c1.distance(c2) <= config.r));
    c1 = new Coord(1, 1);
    c2 = new Coord(1, 10000);
    System.out.println("Distance: " + c1.distance(c2));
    System.out.println("Should be false due: " + (c1.distance(c2) <= config.r));

  }

  public static void runDebugSim() throws IOException {
    new SignalList();
    Signal actSignal;

    Config config = getDebugConfig();

    Gateway gateway = new Gateway(config.r, config.timeBetweenSamples, config.Tp);
    SignalList.SendSignal(MEASURE, gateway, time + config.timeBetweenSamples);

    // start sensors cycles
    for (int i = 0; i < config.n; i++) {
      int x = config.xs[i];
      int y = config.ys[i];
      Coord coord = new Coord(x, y);
      Sensor sensor = new Sensor(coord, gateway, config.ts);
      // stochastic, otherwise a lot of collisions immediately
      SignalList.SendSignal(SEND_MESSAGE, sensor, time);
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

    double meanSum = gateway.zs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    double mean = (1.0 / config.nbrOfMeasurements) * meanSum;

    System.out.println("nbr of transmissions: " + gateway.nbrOfTransmissions);
    System.out.println("nbr of success: " + gateway.nbrOfSuccess);
    System.out.println(mean);
  }

  static Config getDebugConfig() {
    int n = 3;
    int ts = 1000;
    double r = 7000;
    double Tp = 10000;
    double timeBetweenSamples = 1000;
    int nbrOfMeasurements = 1;

    ArrayList<Integer> xlist = new ArrayList<>();
    ArrayList<Integer> ylist = new ArrayList<>();
    xlist.add(1);
    xlist.add(1);
    xlist.add(1);
    ylist.add(1);
    ylist.add(2);
    ylist.add(3);

    Integer[] xs = xlist.toArray(new Integer[n]);
    Integer[] ys = ylist.toArray(new Integer[n]);

    return new Config(n, ts, Tp, r, timeBetweenSamples, nbrOfMeasurements, xs, ys);
  }
}