import java.util.*;
import java.io.*;

class State extends GlobalSimulation {

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements

	public double totalTime = 0, interarrivalTime = 0;
	private boolean[] status = new boolean[5];

	Random slump = new Random(); // This is just a random number generator

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case ARRIVAL:
				arrival();
				break;
			case READY1:
				ready1();
				break;
			case READY2:
				ready2();
				break;
			case READY3:
				ready3();
				break;
			case READY4:
				ready4();
				break;
			case READY5:
				ready5();
				break;
		}
	}

	// The following methods defines what should be done when an event takes place.
	// This could
	// have been placed in the case in treatEvent, but often it is simpler to write
	// a method if
	// things are getting more complicated than this.

	private void arrival() {
		insertEvent(READY1, time + 1 + 4.0 * slump.nextDouble());
		insertEvent(READY2, time + 1 + 4.0 * slump.nextDouble());
		insertEvent(READY3, time + 1 + 4.0 * slump.nextDouble());
		insertEvent(READY4, time + 1 + 4.0 * slump.nextDouble());
		insertEvent(READY5, time + 1 + 4.0 * slump.nextDouble());
	}

	private void ready1() {
		status[0] = true;
		insertEvent(READY2, time);
		insertEvent(READY5, time);
	}

	private void ready2() {
		status[1] = true;
	}

	private void ready3() {
		status[2] = true;
		insertEvent(READY4, time);
	}

	private void ready4() {
		status[3] = true;
	}

	private void ready5() {
		status[4] = true;
	}

	public boolean allBroken() {
		return (status[0] && status[1] && status[2] && status[3] && status[4]);
	}
}