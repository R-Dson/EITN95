import java.util.*;
import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {

		Event actEvent;
		State actState = new State(); // The state that shoud be used
		double totalTime = 0;

		// 1000 runs of the system
		for (int i = 0; i < 1000; i++) {
			insertEvent(ARRIVAL, 0);

			// The main simulation loop
			while (!actState.allBroken()) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}

			totalTime += GlobalSimulation.time;
			GlobalSimulation.time = 0;
			GlobalSimulation.eventList = new EventListClass();
			actEvent = null;
			actState = new State();
		}

		// Some events must be put in the event list at the beginning

		System.out.println(totalTime / 1000);

	}
}