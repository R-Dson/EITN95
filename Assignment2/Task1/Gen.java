import java.util.*;
import java.io.*;

class Gen extends Proc{

	Random slump = new Random();

	public ArrayList<QS> listSendTo = new ArrayList<>();
	public double lambda; 
	public static int totalArrived = 0, totalNumMeasures = 0;
	
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				QS reciever = getFirstAvailable();
				if (reciever != null) {
					System.out.println("ID: " + reciever.ID + ", NumArrived: " + totalArrived + ", numMeasures: " + totalNumMeasures);
					SignalList.SendSignal(ARRIVAL, reciever, time);
				}
				// next arrival
				SignalList.SendSignal(READY, this, time + QS.expLambda(lambda));
			} break;
			case MEASURE:{
				for (QS q : listSendTo) {
					if (q.occupied)
						totalArrived += 1;
				}
				totalNumMeasures++;
				SignalList.SendSignal(MEASURE, this, time+1);
				break;
			}
		}
	}

	public QS getFirstAvailable() {
		for (QS q : listSendTo) {
			if (!q.occupied)
				return q;
			// else denied;
		}
		return null;
	}
}