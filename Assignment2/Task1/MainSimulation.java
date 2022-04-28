import java.util.*;
import java.io.*;

public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	Signal actSignal;
    	new SignalList();

    	Gen Generator = new Gen();
    	Generator.lambda = 8; 
		int Nservers = 1000;
		int ServiceTimeX = 100;

		for (int i = 0; i < Nservers; i++) {
			QS Qtemp = new QS();
			Qtemp.ID = i;
			Qtemp.ServiceTimeX = ServiceTimeX;
			Generator.listSendTo.add(Qtemp);
		}

    	SignalList.SendSignal(READY, Generator, 0);
		SignalList.SendSignal(MEASURE, Generator, 1);

    	while (Gen.totalNumMeasures < 1000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}
		
    	System.out.println("Time:" + time);
		System.out.println("------------------------------------------------------");
    	System.out.println("Mean arrived: " + 1.0*Gen.totalArrived/Gen.totalNumMeasures);
		System.out.println("totalArrived: " + 1.0*Gen.totalArrived);
		System.out.println("totalNumMeasures: " + 1.0*Gen.totalNumMeasures);

    }
}