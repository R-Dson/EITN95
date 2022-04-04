import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue = 0, accumulatedA = 0, accumulatedB = 0, noMeasurements = 0;

	private int bufferA = 0, bufferB = 0, d=1;
	private double xa = 0.002, xb=0.004, lambda=1/150d;

	Random slump = new Random(); // This is just a random number generator
	
	
	// The following method is called by the main program each time a new event has been fetched
	// from the event list in the main loop. 
	public void treatEvent(Event x){
		switch (x.eventType){
			case ARRIVALA:
				arrivalA();
				break;
			case ARRIVALB:
				arrivalB();
				break;
			case READY:
				ready();
				break;
			case MEASURE:
				measure();
				break;
		}
	}
	
	// The following methods defines what should be done when an event takes place. This could
	// have been placed in the case in treatEvent, but often it is simpler to write a method if 
	// things are getting more complicated than this.
	
	private void arrivalA(){
		// if there is no B signal then process A signal
		bufferA++;
		accumulatedA++;

		if (bufferB == 0){
			// ready for next signal
			insertEvent(READY, time + xa);
		}
		
		// recieve next A signal
		insertEvent(ARRIVALA, time + exp(lambda));
	}

	private void arrivalB(){
		// add to B buffer
		bufferB++;
		accumulatedB++;
		// time until ready again
		insertEvent(READY, time + xb);
	}
	
	private void ready(){
		if (bufferB == 0 && bufferA > 0)
		{
			bufferA--;
			// sending back the disconnect signal
			insertEvent(ARRIVALB, time + d);
			// processing
		}
		else // else add to buffer
		{
			if (bufferB > 0)
				bufferB--;
		}
		if (bufferA > 0)
			insertEvent(READY, time + xa);
	}
	
	private void measure(){
		//accumulated = accumulatedA;
		System.out.println("Buffer A:" + bufferA + " , Buffer B:" + bufferB);
		noMeasurements++;
		insertEvent(MEASURE, time + 0.1);
	}

	private double exp(double mean) {
		return Math.log(1-slump.nextDouble())/-(1/mean);
	}

}
