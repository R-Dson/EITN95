import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	//Signallistan startas och actSignal deklareras. actSignal �r den senast utplockade signalen i huvudloopen nedan.
    	// The signal list is started and actSignal is declaree. actSignal is the latest signal that has been fetched from the 
    	// signal list in the main loop below.

    	Signal actSignal;
    	new SignalList();

    	//H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges v�rden.
    	// Here process instances are created (two queues and one generator) and their parameters are given values. 

    	QS Q1 = new QS();
    	Q1.sendTo = null;

//    	Gen Generator = new Gen();
//    	Generator.lambda = 9; //Generator ska generera nio kunder per sekund  //Generator shall generate 9 customers per second
//    	Generator.sendTo = Q1; //De genererade kunderna ska skickas till k�systemet QS  // The generated customers shall be sent to Q1

    	//H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.
    	//To start the simulation the first signals are put in the signal list

    	//SignalList.SendSignal(READY, Generator, time);
    	
 
    	for(int i = 0;i<20;i++) {
    		Person p = new Person(2,i);
    		Q1.list.add(p);
    		SignalList.SendSignal(MOVE, p, Q1, time);
    	}

    	
    //	SignalList.SendSignal(MOVE, p1, Q1, time);
    //	SignalList.SendSignal(MOVE, p1, Q1, time);
    	
    	
    	while (!Q1.done()){
    		System.out.println(time);
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}
    	for(int i = 0;i<20;i++) {
    	System.out.println(Q1.list.get(i).printMeetings());
    
    	}
    }
}