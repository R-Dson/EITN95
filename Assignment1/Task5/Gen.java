import java.util.*;
import java.io.*;

class Gen extends Proc {

	Random slump = new Random();


	// dispatcher time assumed to be 0
	public ArrayList<QS> listSendTo = new ArrayList<>();
	public double mean;  
	public static int totalArrived = 0, totalQueueMeasure = 0, totalNumMeasures = 0;
	public int totalInQueue = 0;
	public int numberMeasurements = 0;

	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				QS reciever = (QS)getRandomProc();
				SignalList.SendSignal(ARRIVAL, reciever, time);
				double tmp = slump.nextDouble()*mean*2;
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