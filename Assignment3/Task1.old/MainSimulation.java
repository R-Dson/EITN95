import java.util.*;
import java.io.*;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation


public class MainSimulation extends Global{

    public static void main(String[] args) throws IOException {

		fileReader f = new fileReader("config.txt");
		f.readFile();
		config c = f.getConfig();

    	Signal actSignal;
    	new SignalList();

    	QS Q1 = new QS();
    	Q1.sendTo = null;

		ArrayList<QS> allNodes = new ArrayList<>();
		for (int i = 0; i < c.n; i++) {
			QS node = new QS();
			node.x = c.x[i];
			node.y = c.y[i];
			node.id = i;
			node.r = c.r;
			allNodes.add(node);
		}

		for (QS node : allNodes) {
			for (QS node2 : allNodes) {
				if (node != node2 && Math.sqrt((node2.x - node.x)^2 + (node2.y - node.y)^2) <= node.r) {
					node.InRange.add(node2);
				}
			}
		}

    	Gen Generator = new Gen();
    	Generator.lambda = 9; 
    	Generator.sendTo = Q1;

    	SignalList.SendSignal(READY, Generator, time);
    	SignalList.SendSignal(MEASURE, Q1, time);


    	while (time < 100000){
    		actSignal = SignalList.FetchSignal();
    		time = actSignal.arrivalTime;
    		actSignal.destination.TreatSignal(actSignal);
    	}

    	System.out.println("Mean number of customers in queuing system: " + 1.0*Q1.accumulated/Q1.noMeasurements);

    }
}