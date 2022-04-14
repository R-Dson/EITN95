import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	static Random slump = new Random();

	public double pSpecial = 0.1, pE = 0.5;
	public int special = 0, totalArrivedSpecial = 0, totalLeftSpecial = 0, 
				normal = 0, totalArrivedNormal = 0, totalLeftNormal = 0, ID = 0;

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				int person = whoArrives();
				switch (person) {
					case 0:
						special++;
						totalArrivedSpecial++;
						Gen.totalArrivedSpecial++;
						break;
					/*case 1:
						special++;
						break;*/
					case 1:
						normal++;
						totalArrivedNormal++;
						Gen.totalArrivedNormal++;
						break;
				}

				numberInQueue++;
				if (numberInQueue == 1){
					SignalList.SendSignal(READY,this, time + expMean(0.5));
				}
			} break;

			case READY:{
				numberInQueue--;
				if (special > 0) {
					special--;
					totalLeftSpecial++;
					Gen.totalLeftSpecial++;
				}
				else if (normal > 0) {
					normal--;
					totalLeftNormal++;
					Gen.totalLeftNormal++;
				}

				/*if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time + exp(5));
				}*/

				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + expMean(0.5));
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				Gen.totalNumMeasures++;
				Gen.totalQueueMeasure = Gen.totalQueueMeasure + numberInQueue;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + expMean(0.5));
			} break;
		}
	}

	private int whoArrives()
	{
		int length = 1000;
		int ran = slump.nextInt(length);
		double prob = pSpecial*length;
		if (ran < prob) 
			return 0;
		//else if (ran <= (pA + pB)*length)
			//return 1;
		else
			return 1;
	}

	// mean = 1/lambda
	public static double expMean(double mean) {
		return Math.log(1-slump.nextDouble())/(-1/mean);
	}

	public static double expLambda(double lambda) {
		return expMean(1/lambda);
	}
}