import java.util.*;
import java.io.*;

public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	Signal actSignal;
    	new SignalList();

    	Gen Generator = new Gen();
    	Generator.lambda = 4; 
		Generator.deltaM = 4;
		int Nservers = 100;
		int ServiceTimeX = 10;
		int numMeasures = 1000;

		for (int i = 0; i < Nservers; i++) {
			QS Qtemp = new QS();
			Qtemp.ID = i;
			Qtemp.ServiceTimeX = ServiceTimeX;
			Generator.listSendTo.add(Qtemp);
		}

    	SignalList.SendSignal(READY, Generator, 0);
		SignalList.SendSignal(MEASURE, Generator, 1);

    	while (Gen.totalNumMeasures < numMeasures){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}
		
    	System.out.println("Time:" + time);
		System.out.println("------------------------------------------------------");
    	System.out.println("Mean arrived: " + 1.0*Gen.totalArrived/Gen.totalNumMeasures);

		double mMean = 0;
		for (Integer i : Generator.inQueue) {
			mMean += i;
		}
		mMean /= Generator.inQueue.size();

		double std = calcStandardDevEstimate(mMean, Generator.inQueue);

        System.out.println();
        System.out.println("Std estimation: \t" + String.format("%.4f", std));
        System.out.println("CI length: \t" + String.format("%.4f", 2 * 1.96 * std));
        System.out.println("95% confidence interval:");
        System.out.println("\t" + String.format("%.4f", mMean) + " ± " + String.format("%.4f", 1.96 * std));

    }

	private static double calcStandardDevEstimate(double mMean, List<Integer> means) {
        double sum = 0;
        int M = means.size();
        for (int i = 0; i < M; i++) {
            sum += Math.pow(means.get(i) - mMean, 2);
        }

        // NOTE! Using Bessel's correction, see wiki
        return Math.sqrt(sum / (double) (M - 1));
    }


}