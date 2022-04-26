import java.util.*;
import java.io.*;

//Denna klass rver Global s att man kan anvnda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal r den senast utplockade signalen i huvudloopen nedan.
    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();

    	//Hr nedan skapas de processinstanser som behvs och parametrar i dem ges vrden.
    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

    	Gen Generator = new Gen();
    	Generator.lambda = 8; 
		//Generator ska generera nio kunder per sekund  //Generator shall generate 9 customers per second
    	//Generator.sendTo = Q1; //De genererade kunderna ska skickas till ksystemet QS  // The generated customers shall be sent to Q1

		int Nservers = 1000;
		int ServiceTimeX = 100;

		for (int i = 0; i < Nservers; i++) {
			QS Qtemp = new QS();
			Qtemp.ID = i;
			Qtemp.ServiceTimeX = ServiceTimeX;
			Generator.listSendTo.add(Qtemp);
		}

    	//Hr nedan skickas de frsta signalerna fr att simuleringen ska komma igng.
    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(READY, Generator, time);
    	for (QS Q : Generator.listSendTo) {
			SignalList.SendSignal(MEASURE, Q, time);
		}

    	// Detta r simuleringsloopen:
    	// This is the main loop

    	while (Gen.totalNumMeasures < 10000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:
    	//Finally the result of the simulation is printed below:

    	System.out.println("Time:" + time);
		System.out.println("------------------------------------------------------");
    	System.out.println("Mean arrived: " + 1.0*Gen.totalArrived/Gen.totalNumMeasures);
		System.out.println("totalArrived: " + 1.0*Gen.totalArrived);
		System.out.println("totalNumMeasures: " + 1.0*Gen.totalNumMeasures);

    }
}