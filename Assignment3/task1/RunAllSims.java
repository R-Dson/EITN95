import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RunAllSims extends Global {

  static int[] ns = { 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000 };

  public static void main(String[] args) throws IOException {
    runAllNs();
  }

  /*
   * Calculates the mean of means of both Tput and Package Loss. These
   * are stored in files, see bottom of method.
   */
  private static void runAllNs() {
    ArrayList<Double> mmTput = new ArrayList<>();
    ArrayList<Double> stdsTput = new ArrayList<>();
    ArrayList<Double> mmPLoss = new ArrayList<>();
    ArrayList<Double> stdsPLoss = new ArrayList<>();

    int iter = 15;
    Config config;
    ArrayList<Double> meansT;
    ArrayList<Double> meansP;
    for (int n : ns) {
      System.out.println("----------------");
      System.err.println("n = " + n);

      config = getConfig(n);

      meansT = new ArrayList<>();
      meansP = new ArrayList<>();

      ArrayList<Double> PLosses = new ArrayList<>();
      boolean overlap = false;

      int i = 0;
      while (!overlap) {
        Container c = runOneSim(config, false);
        
        meansT.add(c.meanTput);
        meansP.add(c.meanPloss);

        PLosses.addAll(c.PLosses);
        if(i > 0) // i > 0 to start with some values
        {
          double[] valuesNew = Statistics.confidenceIntervalValues(c.PLosses);
          double[] valuesOld = Statistics.confidenceIntervalValues(PLosses);

          double diffHigh = (valuesNew[0] - valuesOld[0]) + 1.96 * Math.sqrt(Math.pow(valuesOld[1], 2) / PLosses.size() + Math.pow(valuesNew[1], 2) / c.PLosses.size());
          double diffLow = (valuesNew[0] - valuesOld[0]) - 1.96 * Math.sqrt(Math.pow(valuesOld[1], 2) / PLosses.size() + Math.pow(valuesNew[1], 2) / c.PLosses.size());
          /*
          double oldHight = valuesOld[0] + 1.96 * valuesOld[1] / Math.sqrt(c.PLosses.size());
          double oldLow = valuesOld[0] - 1.96 * valuesOld[1] / Math.sqrt(c.PLosses.size());

          double newHight = valuesNew[0] + 1.96 * valuesNew[1] / Math.sqrt(PLosses.size());
          double newlow = valuesNew[0] - 1.96 * valuesNew[1] / Math.sqrt(PLosses.size());
          */
          // if they contain 0 then they overlap
          if (diffLow > 0 || diffHigh < 0) {
          //if (oldHight < newlow || oldLow > newHight){
            overlap = true;
          }
          System.out.println("iteration: " + i +" (" + String.format("%.3f", diffLow) + ", " + String.format("%.3f", diffHigh) + ")");
        }

        i++;
      }

      /*for (int i = 0; i < iter; i++) {
        System.out.println("iteration: " + i);
        ArrayList<Double> res = runOneSim(config, true);
        meansT.add(res.get(0));
        meansP.add(res.get(1));
      }*/

      double mMean = Statistics.calcMean(meansT);
      mmTput.add(mMean);
      stdsTput.add(Statistics.calcStandardDevEstimate(mMean, meansT));
      
      mMean = Statistics.calcMean(meansP);
      mmPLoss.add(mMean);
      stdsPLoss.add(Statistics.calcStandardDevEstimate(mMean, meansP));

      System.out.println("----------------");
    }

    writeResultsToFile(mmTput, "a3p3_Tput_mMean.txt");
    writeResultsToFile(stdsTput, "a3p3_Tput_stds.txt");
    writeResultsToFile(mmPLoss, "a3p3_PLoss_mMean.txt");
    writeResultsToFile(stdsPLoss, "a3p3_PLoss_stds.txt");
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

  static class Container
  {
    double meanTput;
    double meanPloss;
    ArrayList<Double> Tputs = new ArrayList<>();
    ArrayList<Double> PLosses = new ArrayList<>();

    public Container(double meanTput, double meanPloss, ArrayList<Double> Tputs, ArrayList<Double> PLosses)
    {
      this.meanPloss = meanPloss;
      this.meanTput = meanTput;
      this.Tputs = Tputs;
      this.PLosses = PLosses;
    }

  }

  public static Container runOneSim(Config config, boolean isVerbose) {
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

    double meanTput = Statistics.calcMean(gateway.Tputs);

    double meanPloss = Statistics.calcMean(gateway.PLosses);

    //ArrayList<Double> results = new ArrayList<>();
    //results.add(meanTput);
    //results.add(meanPloss);

    if (isVerbose) {
      Statistics.confidenceIntervalValues(gateway.PLosses);
      System.out.println("nbr of transmissions: " + gateway.nbrOfTransmissions);
      System.out.println("nbr of success: " + gateway.nbrOfSuccess);
      System.out.println("success ratio: " + (double) gateway.nbrOfSuccess / (double) gateway.nbrOfTransmissions);
    }
    
    return new Container(meanTput, meanPloss, gateway.Tputs, gateway.PLosses);
  }

  static Config getConfig(int n) {
    int ts = 4000;
    double r = 7000;
    double Tp = 1;
    double timeBetweenSamples = 4000;
    int nbrOfMeasurements = 10;

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