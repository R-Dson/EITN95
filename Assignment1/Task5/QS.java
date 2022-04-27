import java.util.*;
import java.io.*;

class QS extends Proc {
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	static Random slump = new Random();

	public double ID = 0;
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				
				if (numberInQueue == 0) {
					SignalList.SendSignal(READY, this, time + expMean(0.5));
				}
				numberInQueue++;
			} break;

			case READY:{
				numberInQueue--;
				if(numberInQueue > 0) {
					SignalList.SendSignal(READY, this, time + expMean(0.5));
				}
				
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