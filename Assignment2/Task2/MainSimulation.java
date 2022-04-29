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
		
    	SignalList.SendSignal(READY, Generator, 0);
    	SignalList.SendSignal(MEASURE, Q1, 0);

    	while (time/24 < days){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

		// print
		// a) ------
		double endTimeday = 0;
		// Generator.endOfDay contains all the end times of the day
		for (Double d : Generator.endOfDay) {
			endTimeday += d;
		}

		double mean = endTimeday / days;
		double std = calcStandardDevEstimate(mean, Generator.endOfDay);
		print("a) ---------------------", mean, std);


		// b) ---------
		double allservice = 0;
		for (Double d : Q1.service) {
			allservice += d;
		}
		double mean2 = allservice / Q1.service.size();
		double std2 = calcStandardDevEstimate(mean2, Q1.service);

		print("b) ---------------------", mean2, std2);
		
		// minutes
		std2 *= 60;
		mean2 *= 60;
		print("In minutes ---------------------", mean2, std2);
    }

	private static void print(String string, double mean, double std)
	{
		System.out.println(string);
    	System.out.println("Std estimation: \t" + String.format("%.4f", std));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mean) + " ± " + String.format("%.4f", 1.96 * std));
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