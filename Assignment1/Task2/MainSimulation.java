import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); 
        insertEvent(ARRIVALA, 0);  
        insertEvent(MEASURE, 0.1);
		insertEvent(READY, 0.002); 
		       
    	while (actState.noMeasurements < 1000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	System.out.println("accumulated bufferA: " + 1.0*actState.accumulatedA + " , accumulated bufferB: " + 1.0*actState.accumulatedB);
    	System.out.println("mean total buffer: " + (double)actState.totalBuffer / (double)actState.noMeasurements);
    }
}