import java.util.*;
import java.io.*;

class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	static Random slump = new Random();
	public ArrayList<ArrayList<Double>> endTime = new ArrayList<>();
	public ArrayList<Double> day = new ArrayList<>();

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				numberInQueue++;
				if (numberInQueue == 1){
					SignalList.SendSignal(READY,this, time + (10 + 10*slump.nextDouble())/60);
				}
			} break;

			case READY:{
				numberInQueue--;
				day.add(time % 24);
				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + (10 + 10*slump.nextDouble())/60);
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 0.5);
			} break;
		}
		
	}

	// mean = 1/lambda
	public static double expMean(double mean) {
		return Math.log(1-slump.nextDouble())/(-1/mean);
	}

	public static double expLambda(double lambda) {
		return expMean(1/lambda);
	}

}