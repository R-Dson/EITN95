import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainSimulation extends Global {

  public static void main(String[] args) throws IOException {
    runAll(15);
  }

  private static void runAll(int iter) throws IOException {
    ArrayList<Double> mmTput = new ArrayList<>();
    ArrayList<Double> stdsTput = new ArrayList<>();
    ArrayList<Double> mmPLoss = new ArrayList<>();
    ArrayList<Double> stdsPLoss = new ArrayList<>();

    ArrayList<Double> meansT;
    ArrayList<Double> meansP;
    for (int i = 0; i < iter; i++) {
      System.out.println("----------------");
      System.out.println("iteration = " + i);

      ArrayList<ArrayList<Double>> res = runOneSim();
      meansT = res.get(0);
      meansP = res.get(1);

      double mMean = Statistics.calcMean(meansT);
      mmTput.add(mMean);
      stdsTput.add(Statistics.calcStandardDevEstimate(mMean, meansT));

      mMean = Statistics.calcMean(meansP);
      mmPLoss.add(mMean);
      stdsPLoss.add(Statistics.calcStandardDevEstimate(mMean, meansP));

      System.out.println("----------------");
    }

    writeResultsToFile(mmTput, "a3p4_Tput_mMean_vary_r.txt");
    writeResultsToFile(stdsTput, "a3p4_Tput_stds_vary_r.txt");
    writeResultsToFile(mmPLoss, "a3p4_PLoss_mMean_vary_r.txt");
    writeResultsToFile(stdsPLoss, "a3p4_PLoss_stds_vary_r.txt");
  }

  public static ArrayList<ArrayList<Double>> runOneSim() throws IOException {
    new SignalList();
    Signal actSignal;

    MyFileReader f = new MyFileReader(configFileName);
    f.readFile();
    Config config = f.getConfig();
    // Config config = getConfig();

    Gateway gateway = new Gateway(config.r, config.timeBetweenSamples, config.Tp);
    SignalList.SendSignal(MEASURE, gateway, time + config.timeBetweenSamples);

    // start sensors cycles
    for (int i = 0; i < config.n; i++) {
      int x = config.xs[i];
      int y = config.ys[i];
      Coord coord = new Coord(x, y);
      Sensor sensor = new Sensor(coord, gateway, config.ts, config.isSmart, config.lb, config.ub);
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

    double mean = Statistics.calcMean(gateway.Tputs);

    // System.out.println("nbr of transmissions: " + gateway.nbrOfTransmissions);
    // System.out.println("nbr of success: " + gateway.nbrOfSuccess);
    // System.out.println(mean);
    ArrayList<ArrayList<Double>> res = new ArrayList<>();

    res.add(gateway.Tputs);
    res.add(gateway.PLosses);

    return res;
  }

  static <T> void writeResultsToFile(Iterable<T> ys, String fileName) {
    File file = new File(fileName);
    try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
      for (T y : ys) {
        fw.write(y + "\n");
      }

      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}