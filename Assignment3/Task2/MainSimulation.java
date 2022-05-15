import java.util.*;
import java.io.*;

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		Signal actSignal;
		new SignalList();

		QS Q1 = new QS();
		Q1.sendTo = null;

		for (int i = 0; i < 20; i++) {
			Person p = new Person(2, i);
			Q1.list.add(p);
			SignalList.SendSignal(MOVE, p, Q1, time);
		}

	
		while (!Q1.done()) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}
		int offset = 1;
		for (int i = 0; i < 20; i++) {
			System.out.println(Q1.list.get(i).printMeetings());
			double[] l = Q1.list.get(i).meetings;
			
			ArrayList<Double> t = new ArrayList<>();
			for (int j = offset; j < l.length; j++) {
				t.add(l[j]);
			}
			offset++;
			writeResultsToFile(t, "m.txt");
		}
	}

	static <T> void writeResultsToFile(Iterable<T> ys, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fw = new FileWriter(file, true);

        for (T y : ys) {
            fw.write(y + "\n");
        }

        fw.close();
    }
}