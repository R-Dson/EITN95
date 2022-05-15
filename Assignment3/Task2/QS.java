import java.util.*;
import java.io.*;

class QS extends Proc {
	public int numberInQueue = 0, accumulated, noMeasurements;
	private int delay = 60;
	public Proc sendTo;
	public ArrayList<Person> list = new ArrayList<Person>();
	Random slump = new Random();

	public void TreatSignal(Signal x) {
		Person person = x.p;

		switch (x.signalType) {
		case MOVE: {

			ArrayList<Person> tmp = new ArrayList<Person>();
			if (person.meeting == false) {
				person.move();

				for (Person p : list) {
					if (p.sameSquare(person)) {
						tmp.add(p);
					}
				}
				if (tmp.size() == 1) {
					SignalList.SendSignal(CENTER, person, this, time + delay + person.timeto());

					person.meeting(tmp.get(0).id, delay);
					tmp.get(0).meeting(person.id, delay);
					tmp.get(0).meeting = true;
				} else {

					SignalList.SendSignal(CENTER, person, this, time + person.timeto());
				}
			} else {

				SignalList.SendSignal(MOVE, person, this, time + delay + person.timeto());
				person.meeting = false;
			}

			break;
		}
		case CENTER: {
			if (person.meeting == false) {
				person.center();
				SignalList.SendSignal(MOVE, person, this, time + person.timeto());
			} else {
				SignalList.SendSignal(CENTER, person, this, time + person.timeto() + delay);
				person.meeting = false;
			}
		}
		}
	}

	public boolean done() {
		for (Person p : list) {
			if (p.done() == false) {
				return false;
			}
		}
		return true;
	}
}