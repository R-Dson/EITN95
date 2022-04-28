import java.util.*;
import java.io.*;

class Gen extends Proc{

	Random slump = new Random();

	public QS sendTo;
	public double lambda;

	int day = 0;
	int lastday = 0;
	
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				if (time < 9)
				{
					time = 9;
					sendTo.day = new ArrayList<>();
				}
				if (day != lastday)
				{
					lastday = day;
					if (sendTo != null)
					{
						sendTo.endTime.add(sendTo.day);
						sendTo.day = new ArrayList<>();
					}
				}
				SignalList.SendSignal(ARRIVAL, sendTo, time);
				
				double time24 = time % 24;
				if (9 <= time24 && time24 <= 17) {
					SignalList.SendSignal(READY, this, time + QS.expLambda(lambda));
				}
				else
				{
					double delta = 33 - time24;
					SignalList.SendSignal(READY, this, time + delta);
					day++;
				}
				
			} break;
		}
	}
}