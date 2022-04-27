import java.util.*;
import java.io.*;

public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

    	Signal actSignal;
    	new SignalList();

    	Gen Generator = new Gen();
    	Generator.mean = 0.15; 

		for (int i = 0; i < 5; i++) {
			QS Qtemp = new QS();
			Qtemp.ID = i;
			Generator.listSendTo.add(Qtemp);
		}

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Generator, 0);

    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}


    	System.out.println(Generator.totalInQueue + " " + Generator.numberMeasurements);
    	System.out.println(1.0*Generator.totalInQueue/Generator.numberMeasurements);


    }
}