import java.util.*;
import java.io.*;


public class MainSimulation extends GlobalSimulation{
 
    public static void main(String[] args) throws IOException {
    	Event actEvent;
    	State actState = new State(); // The state that shoud be used
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Enter interarrival time. 1,1 1,5 or 2 used for the task ");
    	actState.interarrivalTime = scanner.nextDouble();
    	// Some events must be put in the event list at the beginning
        insertEvent(ARRIVAL1, 0);  
        insertEvent(MEASURE, 5);
        
        // The main simulation loop
    	while (actState.noMeasurements < 2000){
    		actEvent = eventList.fetchEvent();
    		time = actEvent.eventTime;
    		actState.treatEvent(actEvent);
    	}
    	
    	// Printing the result of the simulation, in this case a mean value
    	System.out.println("Simulationresult for interarrival time of " + actState.interarrivalTime);
    	System.out.println();
    	System.out.println("Mean number of customers: " + 1.0*actState.accumulated/actState.noMeasurements);

    	System.out.println("Mean time in queue " + actState.totalTime/actState.numberArrived);
    }
}