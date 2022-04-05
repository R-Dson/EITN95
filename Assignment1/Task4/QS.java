import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	public Proc sendTo;
	static Random slump = new Random();

	private double pA = 0.1, pB = 0.2, pE = 0.5;
	public int special = 0, normal = 0;

	public void TreatSignal(Signal x){
		switch (x.signalType){

			case ARRIVAL:{
				
				int person = whoArrives();
				switch (person) {
					case 0:
						special++;
						break;
					case 1:
						special++;
						break;
					case 2:
						normal++;
						break;
				}

				numberInQueue++;
				if (numberInQueue == 1){
					SignalList.SendSignal(READY,this, time + exp(4));
				}
			} break;

			case READY:{
				numberInQueue--;
				if (special > 0) {
					special--;
				}
				else if (normal > 0)
				{
					normal--;
				}

				if (sendTo != null){
					SignalList.SendSignal(ARRIVAL, sendTo, time + exp(5));
				}

				if (numberInQueue > 0){
					SignalList.SendSignal(READY, this, time + exp(4));
				}
			} break;

			case MEASURE:{
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + exp(4));
			} break;
		}
	}

	private int whoArrives()
	{
		int length = 10;
		int ran = slump.nextInt(length);

		if (ran <= pA*length) 
			return 0;
		else if (ran <= (pA + pB)*length)
			return 1;
		else
			return 2;
	}

	public static double exp(double mean) {
		return Math.log(1-slump.nextDouble())/-(1/mean);
	}
}