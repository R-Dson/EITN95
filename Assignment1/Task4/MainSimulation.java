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

    	QS Q1 = new QS();
    	Q1.sendTo = null;
		// probability of special person
		Q1.pSpecial = 0.1;

    	Gen Generator = new Gen();
    	Generator.lambda = 5; //Generator ska generera nio kunder per sekund  //Generator shall generate 9 customers per second
    	Generator.sendTo = Q1; //De genererade kunderna ska skickas till ksystemet QS  // The generated customers shall be sent to Q1

    	//Hr nedan skickas de frsta signalerna fr att simuleringen ska komma igng.
    	//To start the simulation the first signals are put in the signal list

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);


    	// Detta r simuleringsloopen:
    	// This is the main loop

    	while ((Q1.totalArrivedNormal+Q1.totalArrivedSpecial) < 1000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	//Slutligen skrivs resultatet av simuleringen ut nedan:
    	//Finally the result of the simulation is printed below:

    	System.out.println("Time:" + time);
    	System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements);
    	System.out.println("Total left: " + (Q1.totalLeftNormal + Q1.totalLeftSpecial));
		System.out.println("------------------------------------------------------");
    	System.out.println("Total number of normal customers arrived: " + Q1.totalArrivedNormal);
    	System.out.println("Total number of normal customers left: " + Q1.totalLeftNormal);
		System.out.println("------------------------------------------------------");
    	System.out.println("Total number of special customers arrived: " + Q1.totalArrivedSpecial);
    	System.out.println("Total number of special customers left: " + Q1.totalLeftSpecial);
		System.out.println("------------------------------------------------------");
		System.out.println("Average queuing time normal: " + time/(Q1.totalLeftNormal));
		System.out.println("Average queuing time special: " + time/(Q1.totalLeftSpecial));

    }
}