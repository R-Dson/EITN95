import java.util.*;
import java.io.*;

class Gen extends Proc{

	Random slump = new Random();

	public ArrayList<QS> listSendTo = new ArrayList<>();
	public double lambda; 
	public static int totalArrived = 0, totalNumMeasures = 0;
	public ArrayList<Integer> inQueue = new ArrayList<>();
	public double deltaM = 0;
	
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				QS reciever = getFirstAvailable();
				if (reciever != null) {
					//System.out.println("ID: " + reciever.ID + ", NumArrived: " + totalArrived + ", numMeasures: " + totalNumMeasures);
					SignalList.SendSignal(ARRIVAL, reciever, time);
				}
				// next arrival
				SignalList.SendSignal(READY, this, time + QS.expLambda(lambda));
			} break;
			case MEASURE:{
				int i = 0;
				for (QS q : listSendTo) {
					if (q.occupied)
					{
						i++;
						totalArrived += 1;
					}
				}
				inQueue.add(i);
				totalNumMeasures++;
				SignalList.SendSignal(MEASURE, this, time+deltaM);
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