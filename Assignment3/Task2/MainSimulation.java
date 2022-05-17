import java.util.*;
import java.io.*;

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {
		Random r = new Random();
		Signal actSignal;
		new SignalList();
		ArrayList<Double> maxtime = new ArrayList<Double>();
		double totaltime = 0;
		double testtime = 0;
		for (int tmp = 0; tmp < 10000; tmp++) {
			QS Q1 = new QS();
			Q1.sendTo = null;

			for (int i = 0; i < 20; i++) {
				Person p = new Person(r.nextInt(7) + 1, i); // change for different speeds
				Q1.list.add(p);
				SignalList.SendSignal(MOVE, p, Q1, time);
			}

			while (!Q1.done()) {
				actSignal = SignalList.FetchSignal();
				time = actSignal.arrivalTime;
				actSignal.destination.TreatSignal(actSignal);
			}

			for (int i = 0; i < 20; i++) {
				testtime += Q1.list.get(i).totalTime();
			}

			int offset = 1;
			for (int i = 0; i < 20; i++) {
				// System.out.println(Q1.list.get(i).printMeetings());
				double[] l = Q1.list.get(i).meetings;

				ArrayList<Double> t = new ArrayList<>();
				for (int j = offset; j < l.length; j++) {
					t.add(l[j] / 60);
				}
				offset++;
				writeResultsToFile(t, "m.txt");
			}
			totaltime += time / 60;
			maxtime.add(time / 60);
			double stddev = standarddev(maxtime, totaltime / tmp);
			double interval = 1.96 * stddev / Math.sqrt(tmp);
			if (interval < 8 && tmp > 10) {
				System.out.println((testtime / 20) / (tmp + 1));
				System.out.println(totaltime / tmp + " +- " + interval);
				break;
			}
			time = 0;
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

	public static double standarddev(ArrayList<Double> list, double mean) {
		double tmp = 0;
		for (Double d : list) {
			tmp += Math.pow(d - mean, 2);

		}

		tmp = tmp / (list.size() - 1);
		tmp = Math.sqrt(tmp);
		return tmp;
	}
}