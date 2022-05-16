import java.util.LinkedList;
import java.util.List;
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

  static void confidenceInterval(List<Double> zs) {
    int N = zs.size();

    double meanSum = zs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    double mMean = meanSum / (double) N;

    double std = calcStandardDevEstimate(mMean, zs);

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
}
