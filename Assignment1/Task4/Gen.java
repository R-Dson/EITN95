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
	public Proc sendTo;    //Anger till vilken process de genererade kunderna ska skickas //Where to send customers
	public double lambda;  //Hur mnga per sekund som ska generas //How many to generate per second

	//Hr nedan anger man vad som ska gras nr en signal kommer //What to do when a signal arrives
	public void TreatSignal(Signal x){
		switch (x.signalType){
			case READY:{
				SignalList.SendSignal(ARRIVAL, sendTo, time);
				SignalList.SendSignal(READY, this, time + QS.expLambda(lambda));}
				break;
		}
	}
}