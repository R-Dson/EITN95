import java.util.Random;

public class Statistics {
  private static Random rand = new Random();

  public static double uniformDistribution(double mean) {
    double rnd = rand.nextDouble();
    return ((mean * 2 - 0) * rnd);
  }

  public static double expDistPdf(double lambda) {
    return (-1.0) * Math.log(1 - rand.nextDouble()) / lambda;
  }

  public static double expMean(double mean) {
    return Math.log(1 - rand.nextDouble()) / (-1 / mean);
  }

}
