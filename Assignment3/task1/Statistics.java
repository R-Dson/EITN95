import java.util.List;
import java.util.Random;

public class Statistics {
  private static Random rand = new Random();

  public static double uniformDistribution(double mean) {
    double rnd = rand.nextDouble();
    return ((mean * 2 - 0) * rnd);
  }

  public static double uniformDistribution(double a, double b) {
    double rnd = rand.nextDouble();
    return ((b - a) * rnd + a);
  }

  public static double expDistPdf(double lambda) {
    return (-1.0) * Math.log(1 - rand.nextDouble()) / lambda;
  }

  public static double expMean(double mean) {
    return Math.log(1 - rand.nextDouble()) / (-1 / mean);
  }

  static double calcMean(List<Double> zs) {
    int N = zs.size();
    double meanSum = zs.stream()
        .reduce(0.0, (subtotal, element) -> subtotal + element);
    return meanSum / (double) N;
  }

  static double[] confidenceIntervalValues(List<Double> zs)
  {
    double mMean = calcMean(zs);
    double std = calcStandardDevEstimate(mMean, zs);
    double[] values = new double[2];
    values[0] = mMean ;
    values[1] = std;
    return values;
  }

  static void confidenceInterval(List<Double> zs) {
    double mMean = calcMean(zs);

    double std = calcStandardDevEstimate(mMean, zs);

    System.out.println();
    System.out.println("Std estimation: \t" + String.format("%.4f", std));
    System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std));
    System.out.println("95% confidence interval:");
    System.out.println("\t" + String.format("%.4f", mMean) + " ± " + String.format("%.4f", 1.96 * std));
  }

  static double calcStandardDevEstimate(double mMean, List<Double> means) {
    double sum = 0;
    int M = means.size();
    for (int i = 0; i < M; i++) {
      sum += Math.pow(means.get(i) - mMean, 2);
    }

    // NOTE! Using Bessel's correction, see wiki
    return Math.sqrt(sum / (double) (M - 1));
  }

  static boolean checkCIOverlap(List<Double> means, List<Double> stds) {
    boolean isOverlap = false;
    for (int i = 0; i < means.size() - 1; i++) {
      for (int j = 1; j < means.size(); j++) {
        double mean = means.get(i);
        double std = stds.get(i);
        double nextMean = means.get(j);
        double nextStd = stds.get(j);

        // check right
        if (mean + 1.96 * std < nextMean - 1.96 * nextStd) {
          System.out.println("95% confidence interval:");
          System.out.println(
              "\t" + String.format("%.4f", mean - 1.96 * std) + " ± " + String.format("%.4f", mean + 1.96 * std));
          System.out.println(
              "\t" + String.format("%.4f", nextMean - 1.96 * nextStd) + " ± "
                  + String.format("%.4f", nextMean + 1.96 * nextStd));
          isOverlap = true;
        }

        // check left
        if (mean - 1.96 * std > nextMean + 1.96 * nextStd) {
          System.out.println("95% confidence interval:");
          System.out.println(
              "\t" + String.format("%.4f", mean - 1.96 * std) + " ± " + String.format("%.4f", mean + 1.96 * std));
          System.out.println(
              "\t" + String.format("%.4f", nextMean - 1.96 * nextStd) + " ± "
                  + String.format("%.4f", nextMean + 1.96 * nextStd));

          isOverlap = true;
        }

      }
    }
    return isOverlap;
  }
}
