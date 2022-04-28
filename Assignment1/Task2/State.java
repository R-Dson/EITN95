import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	
	public int numberInQueue = 0, accumulatedA = 0, accumulatedB = 0, noMeasurements = 0, totalBuffer = 0;

	private int bufferA = 0, bufferB = 0, d = 1;
	private double xa = 0.002, xb = 0.004, lambda = 1/150d;

	Random slump = new Random(); 
	
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
	
	private void arrivalA(){
		// if there is no B signal then process A signal
		bufferA++;
		accumulatedA++;
		
		// recieve next A signal
		insertEvent(ARRIVALA, time + exp(lambda));
	}

	private void arrivalB(){
		// add to B buffer
		bufferB++;
		accumulatedB++;
	}
	
	private void ready(){
		double delta = xa;
		
		// yes
		if (bufferA > 0){
			bufferA--;
			insertEvent(ARRIVALB, time + d);
		}
		else if (bufferB > 0) {
			bufferB--;
			delta = xb;
		}
		// no
		//else 
		
		insertEvent(READY, time + delta);
	}
	
	private void measure(){
		//accumulated = accumulatedA;
		totalBuffer += (bufferA + bufferB);
		//System.out.println("Buffer A:" + bufferA + " , Buffer B:" + bufferB);
		noMeasurements++;
		insertEvent(MEASURE, time + 0.1);
	}

	private double exp(double mean) {
		return Math.log(1-slump.nextDouble())/-(1/mean);
	}

}
