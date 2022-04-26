import java.util.*;
import java.io.*;

//Denna klass rver Proc, det gr att man kan anvnda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc{

	//Slumptalsgeneratorn startas:
	//The random number generator is started:
	Random slump = new Random();

	//Generatorn har tv parametrar:
	//There are two parameters:
	//public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers

	// dispatcher time assumed to be 0
	public ArrayList<QS> listSendTo = new ArrayList<>();
	public double lambda;  //Hur mnga per sekund som ska generas //How many to generate per second
	public static int totalArrived = 0, totalNumMeasures = 0;
	//Hr nedan anger man vad som ska gras nr en signal kommer //What to do when a signal arrives

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				QS reciever = getFirstAvailable();
				if (reciever != null) {
					//
					if (totalArrived > 0)
						System.out.println("ID: " + reciever.ID + ", NumArrived: " + totalArrived + ", numMeasures: " + totalNumMeasures);
					SignalList.SendSignal(ARRIVAL, reciever, time);
				}
				// next arrival
				SignalList.SendSignal(READY, this, time + QS.expLambda(lambda));
			} break;
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