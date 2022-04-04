import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue1 = 0, accumulated = 0, noMeasurements = 0, numberInQueue2 = 0,numberArrived = 0, numberRejected = 0, numberLeft = 0;
	public double totalTime = 0, interarrivalTime = 0;
	public ArrayList<Double> timelist = new ArrayList<Double>();
	
	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
		case ARRIVAL1:
			arrival1();
			break;
		case ARRIVAL2:
			arrival2();
			break;
		case READY1:
			ready1();
			break;
		case READY2:
			ready2();
			break;
		case MEASURE:
			measure();
			break;
		}
	}
	
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
private void arrival1(){
		
	
		if (numberInQueue1 == 0)
			insertEvent(READY1, time + exp(1));
		numberInQueue1++;
		
		timelist.add(time);
		numberArrived++;
		insertEvent(ARRIVAL1, time + exp(interarrivalTime));
		
	}
	private void ready1(){
		numberInQueue1--;
		if (numberInQueue1 > 0)
			insertEvent(READY1, time + exp(1));
		insertEvent(ARRIVAL2, time);
	}
	
	private void arrival2(){
		if (numberInQueue2 == 0)
			insertEvent(READY2, time + exp(1));
		numberInQueue2++;
		
		}
	
	
	
	private void ready2(){
		numberInQueue2--;
		totalTime = totalTime+time-timelist.remove(0);
	
		
		if (numberInQueue2 > 0)
			insertEvent(READY2, time + exp(1));
	}
	private void measure(){
		accumulated = accumulated +numberInQueue1 + numberInQueue2;
		noMeasurements++;
		insertEvent(MEASURE, time + exp(5));
	}
	
	private double exp(double mean) {
		return Math.log(1-slump.nextDouble())/-(1/mean);
	}
}