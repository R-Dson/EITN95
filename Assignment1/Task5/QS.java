import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc {
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	static Random slump = new Random();

	public double ID = 0;

	private boolean first = true;
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				numberInQueue++;
				if (first){
					SignalList.SendSignal(READY, this, time);
					first = false;
				}
			} break;

			case READY:{
				if (numberInQueue > 0) {
					numberInQueue--;
				}
				
				SignalList.SendSignal(READY, this, time + expMean(0.5));
			} break;

			case MEASURE:{
				noMeasurements++;

				Gen.totalNumMeasures++;
				Gen.totalQueueMeasure = Gen.totalQueueMeasure + numberInQueue;

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