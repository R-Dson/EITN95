import java.util.*;
import java.io.*;


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	Signal actSignal;
    	new SignalList();

    	QS Q1 = new QS();
    	Q1.sendTo = null;
		Q1.pSpecial = 0.8;

    	Gen Generator = new Gen();
    	Generator.lambda = 0.2; 
    	Generator.sendTo = Q1; 


    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time + 0.1);


    	while ((Q1.totalArrivedNormal + Q1.totalArrivedSpecial) < 1000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

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
		System.out.println("Average queuing time normal: " + (1.0*Q1.totalArrivedNormal - 1.0*Q1.totalLeftNormal)/time);
		System.out.println("Average queuing time special: " + (1.0*Q1.totalArrivedSpecial - 1.0*Q1.totalLeftSpecial)/time);

    }
}