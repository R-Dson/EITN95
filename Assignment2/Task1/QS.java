import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public Proc sendTo;
	static Random slump = new Random();

	public boolean occupied = false;
	public int ID = 0, ServiceTimeX = 0;

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case ARRIVAL:{
				occupied = true;
				SignalList.SendSignal(READY, this, time + ServiceTimeX);
			} break;
			case READY:{
				occupied = false;
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