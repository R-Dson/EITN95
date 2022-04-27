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
				normal = 0, totalArrivedNormal = 0, totalLeftNormal = 0;

	private boolean first = true;
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				int person = whoArrives();
				switch (person) {
					case 0:
						special++;
						totalArrivedSpecial++;
						break;
					case 1:
						normal++;
						totalArrivedNormal++;
						break;
				}

				numberInQueue++;
				
				if (first){
					SignalList.SendSignal(READY, this, time);
					first = false;
				}
			} break;

			case READY:{
				if (special > 0) {
					special--;
					totalLeftSpecial++;
					numberInQueue--;
				}
				else if (normal > 0) {
					normal--;
					totalLeftNormal++;
					numberInQueue--;
				}
				SignalList.SendSignal(READY, this, time + expMean(4));
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + 0.1);
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