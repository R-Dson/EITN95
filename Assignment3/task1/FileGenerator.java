import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileGenerator extends Global {
    String aNewLine = "\r\n";

    FileWriter fileWriter;
    Config c;

    public static void main(String[] args) {
        int n = 1000;
        int ts = 4000;
        double r = 7000;
        double Tp = 1;
        double timeBetweenSamples = 1000;
        int nbrOfMeasurements = 1000;
        boolean isSmart = true;
        double lb = ts * 0.1;
        double ub = ts * 0.5;

        Random slump = new Random();
        ArrayList<Integer> xlist = new ArrayList<>();
        ArrayList<Integer> ylist = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int xSlump = slump.nextInt(10000);
            int ySlump = slump.nextInt(10000);

            // Can't overlap
            while (xlist.contains(xSlump) && ylist.contains(ySlump)) {
                xSlump = slump.nextInt(10000);
                ySlump = slump.nextInt(10000);
            }

            xlist.add(xSlump);
            ylist.add(ySlump);

        }

        Integer[] xs = xlist.toArray(new Integer[n]);
        Integer[] ys = ylist.toArray(new Integer[n]);

        Config c = new Config(n, ts, Tp, r, timeBetweenSamples, nbrOfMeasurements, xs, ys, isSmart, lb, ub);

        FileGenerator fg = new FileGenerator(c);
        fg.printConfig();
    }

    FileGenerator(Config c) {
        this.c = c;
        try {
            fileWriter = new FileWriter(configFileName, true);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void printConfig() {

        println("n=" + Integer.toString(c.n) + aNewLine);
        println("ts=" + Integer.toString(c.ts) + aNewLine);

        println("Tp=" + Double.toString(c.Tp) + aNewLine);
        println("r=" + Double.toString(c.r) + aNewLine);
        println("timeBetweenSamples=" + Double.toString(c.timeBetweenSamples) + aNewLine);
        println("nbrOfMeasurements=" + Integer.toString(c.nbrOfMeasurements) + aNewLine);
        println("isSmart=" + Boolean.toString(c.isSmart) + aNewLine);
        println("lb=" + Double.toString(c.lb) + aNewLine);
        println("ub=" + Double.toString(c.ub) + aNewLine);

        println("xs=");
        for (Integer x : c.xs) {
            if (x != c.xs[c.xs.length - 1])
                println(Integer.toString(x) + ",");
            else
                println(Integer.toString(x) + aNewLine);
        }

        println("ys=");
        for (Integer y : c.ys) {
            if (y != c.ys[c.ys.length - 1])
                println(Integer.toString(y) + ",");
            else
                println(Integer.toString(y));
        }

        try {
            fileWriter.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void println(String string) {
        try {
            fileWriter.write(string);
        } catch (IOException pIOE) {
            System.out.println(pIOE.toString());
        }
    }

}
