import java.util.*;
import java.io.*;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc{
	public int numberInQueue = 0, accumulated, noMeasurements;
	private int delay = 5;
	public Proc sendTo;
	public ArrayList<Person> list = new ArrayList<Person>();
	Random slump = new Random();

	public void TreatSignal(Signal x){
		Person person = x.p;
		switch (x.signalType){

		
			case MOVE:{
				for(Person p: list) {
					if(p.sameSquare(person)) {
						p.meeting(person.id, delay);
						person.meeting(p.id, delay);
						System.out.println(p + " " + person);
					}
				}
				person.move();
			//	System.out.println(person);
				SignalList.SendSignal(CENTER, person, this, time+person.timeto());

		 break;
		}	case CENTER:{
			person.center();
			SignalList.SendSignal(MOVE, person, this, time+person.timeto());
		}
	}
}
	public boolean done() {
		for(Person p: list) {
			if(p.done()==false) {
				return false;
			}
		}
		return true;
	}
}