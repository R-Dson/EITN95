import java.util.*;
import java.io.*;


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	Signal actSignal;
    	new SignalList();
    	QS Q1 = new QS();

    	Gen Generator = new Gen();
    	Generator.lambda = 4;
		Generator.sendTo = Q1; 
		int days = 1000;
		
    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);

    	while (time/24 < days){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		// a) ------
		int day = 1;
		double endTimeday = 0;
		// Q1.endTime contains all the times of services
		List<Double> ends = new LinkedList<>();
		for (ArrayList<Double> list : Q1.endTime) {
			//System.out.println("Day: " + day);
			/*for (Double d : ald) {
				System.out.println(d);
			}*/

			double end = list.get(list.size() - 1);
			ends.add(end);
			endTimeday += end;

			day++;
		}
		
		double mean = endTimeday / days;
		
    	System.out.println("a) ---------------------");
		double std = calcStandardDevEstimate(mean, ends);
    	System.out.println("Std estimation: \t" + String.format("%.4f", std));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mean) + " ± " + String.format("%.4f", 1.96 * std));
		
		// b) ---------
		System.out.println("b) ---------------------");
		double allservice = 0;
		for (Double d : Q1.service) {
			allservice += d;
		}

		double mean2 = allservice / Q1.service.size();

		double std2 = calcStandardDevEstimate(mean2, Q1.service);
    	System.out.println("Std estimation: \t" + String.format("%.4f", std2));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std2));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mean2) + " ± " + String.format("%.4f", 1.96 * std2));
		// minutes
		System.out.println("In minutes ---------------------");
		std2 *= 60;
		mean2 *= 60;
    	System.out.println("Std estimation: \t" + String.format("%.4f", std2));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std2));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mean2) + " ± " + String.format("%.4f", 1.96 * std2));
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