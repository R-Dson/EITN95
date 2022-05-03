import java.util.*;
import java.io.*;

class State extends GlobalSimulation{
	public int numberInQueue1 = 0, accumulated = 0, noMeasurements = 0, numberInQueue2 = 0, interarrivalTime = 2,numberArrived = 0, numberRejected = 0;

	Random slump = new Random();
	
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
	
	private void arrival1(){		
		if(numberInQueue1<10) {
			if (numberInQueue1 == 0)
				insertEvent(READY1, time + exp(2.1));
			numberInQueue1++;
		}
		else {
			numberRejected++;
		}
		numberArrived++;
		insertEvent(ARRIVAL1, time + interarrivalTime);	
	}

	private void ready1(){
		numberInQueue1--;
		if (numberInQueue1 > 0)
			insertEvent(READY1, time + exp(2.1));
		insertEvent(ARRIVAL2, time);
	}
	
	private void arrival2(){
		if (numberInQueue2 == 0)
			insertEvent(READY2, time + 2);
		numberInQueue2++;
	}
	
	private void ready2(){
		numberInQueue2--;
		if (numberInQueue2 > 0)
			insertEvent(READY2, time + 2);
	}

	private void measure(){
		accumulated = accumulated + numberInQueue2;
		noMeasurements++;
		insertEvent(MEASURE, time + exp(5));
	}
	
	private double exp(double mean) {
		return Math.log(1-slump.nextDouble())/-(1/mean);
	}
}