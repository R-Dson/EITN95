import java.util.*;
import java.io.*;

//Denna klass rver Proc, det gr att man kan anvnda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc {

	//Slumptalsgeneratorn startas:
	//The random number generator is started:
	Random slump = new Random();

	//Generatorn har tv parametrar:
	//There are two parameters:
	//public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers

	// dispatcher time assumed to be 0
	public ArrayList<QS> listSendTo = new ArrayList<>();
	public double mean;  //Hur mnga per sekund som ska generas //How many to generate per second
	public static int totalArrived = 0, totalQueueMeasure = 0, totalNumMeasures = 0;
	public int totalInQueue = 0;
	public int numberMeasurements = 0;
	//Hr nedan anger man vad som ska gras nr en signal kommer //What to do when a signal arrives

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				QS reciever = (QS)getRandomProc();
				SignalList.SendSignal(ARRIVAL, reciever, time);
				double tmp = slump.nextDouble()*0.11*2;
				SignalList.SendSignal(READY, this, time+tmp);}
				break;
			case MEASURE:{
				for(int i = 0;i<5;i++) {
					totalInQueue+=listSendTo.get(i).numberInQueue;
				}
			
				numberMeasurements++;
				SignalList.SendSignal(MEASURE, this, time+1);
				break;
			}
		}
	}

	private Proc getRandomProc()
	{
		// if there is 5 available processes
		return listSendTo.get(slump.nextInt(5));
	}

	private Proc getRoundRobinProc()
	{
		// start at 0
		int selected = totalArrived % 5;
		totalArrived++;
		return listSendTo.get(selected);
	}

	private Proc getSmallestProc()
	{
		QS min = listSendTo.get(0);
		for (int i = 1; i < listSendTo.size(); i++) {
			if (listSendTo.get(i).numberInQueue < min.numberInQueue) {
				min = listSendTo.get(i);
			}
		}
		return min;
	}
}