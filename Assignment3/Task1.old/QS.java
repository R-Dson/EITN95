import java.util.*;
import java.io.*;

class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	static Random slump = new Random();

	public int x, y, id;
	public double r;
	public boolean isTransmitting = false;

	public ArrayList<QS> InRange = new ArrayList<>();

	public void TreatSignal(Signal x){
		switch (x.signalType){

			case ARRIVAL:{
				numberInQueue++;
				if (numberInQueue == 1){
					SignalList.SendSignal(READY,this, time + 0.2*slump.nextDouble());
				}
			} break;

			case WAKE:{

			} break;

			case READY:{
				numberInQueue--;
				if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}
				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + 0.2*slump.nextDouble());
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 2*slump.nextDouble());
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