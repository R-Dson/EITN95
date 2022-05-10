
import java.io.*;
import java.util.Random;

public class FileGenerator {
    String name = "config.txt";
    String aNewLine = "\r\n";

    FileWriter fileWriter;
    config c;

    public static void main(String[] args) {
        int n = 1000;
        int ts = 4000;
        double r = 7000;
        double Tp = 1;

        Random slump = new Random();
        Integer[] x = new Integer[n];
        Integer[] y = new Integer[n];

        for (int i = 0; i < n; i++) {
            x[i] = slump.nextInt(10000);
            y[i] = slump.nextInt(10000);
        }

        config c = new config(n, ts, Tp, r, x, y);

        FileGenerator fg = new FileGenerator(c);
        fg.printConfig();
    }

    FileGenerator(config c)
    {
        this.c = c;
        try {
            fileWriter = new FileWriter(name, true);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void printConfig()
    {
        
        println("n=" + Integer.toString(c.n) + aNewLine);
        println("ts=" + Integer.toString(c.ts) + aNewLine);

        println("Tp=" + Double.toString(c.Tp) + aNewLine);
        println("r=" + Double.toString(c.r) + aNewLine);

        println("xs=");
        for (Integer x : c.x) {
            if (x != c.x[c.x.length-1])
                println(Integer.toString(x) + ",");
            else
                println(Integer.toString(x) + aNewLine);
        }

        println("ys=");
        for (Integer y : c.y) {
            if (y != c.y[c.y.length-1])
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

    public void println(String string)
	{		
		try
		{
			fileWriter.write(string);
        }
        catch(IOException pIOE)
        {
        	System.out.println(pIOE.toString());
        }       
	}

}
