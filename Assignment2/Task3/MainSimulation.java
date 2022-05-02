import java.util.*;
import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {

		Event actEvent;
		State actState = new State();

		insertEvent(MONTH, 0);
		insertEvent(SPECIAL, 0 + actState.slump.nextInt(96));

		double stddev = 0;
		double mean = 0;
		double tmp = 0;
		double nbriterations = 10; //how many iterations every sample is made out of. currently set to 10. more iterations needed for higher savingrate
		ArrayList<Double> samplelist = new ArrayList<Double>();
		for (int i = 0; i < Integer.MAX_VALUE; i++) { //loop breaks when interval<1
			for (int j = 0; j < nbriterations; j++) { 

				while (actState.money < 2000000) {
					actEvent = eventList.fetchEvent();
					time = actEvent.eventTime;
					actState.treatEvent(actEvent);

				}
				tmp += time;
				actState = new State();
				insertEvent(MONTH, 0);
				insertEvent(SPECIAL, 0 + actState.slump.nextInt(96));
				time = 0;

			}
			
			double samplemean = tmp / nbriterations;
			samplelist.add(samplemean);
			mean = mean(samplelist);
			stddev = standarddev(samplelist, mean);
			double interval = 1.96 * stddev / Math.sqrt(i);
		
			if (interval < 1) { //when to stop simulation
			System.out.println("number of simulations ran: " + i);
			System.out.println(mean + " +- " + interval);
				break;
			}
			tmp = 0;
			samplemean = 0;

		}

	}

	public static double mean(ArrayList<Double> list) {
		double tmp = 0;
		for (Double d : list) {
			tmp += d;
		}
		return tmp / list.size();
	}

	public static double standarddev(ArrayList<Double> list, double mean) {
		double tmp = 0;
		for (Double d : list) {
			tmp += Math.pow(d - mean, 2);

		}

		tmp = tmp / (list.size() - 1);
		tmp = Math.sqrt(tmp);
		return tmp;
	}
}