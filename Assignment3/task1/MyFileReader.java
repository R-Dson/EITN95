import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class MyFileReader extends Global {
    private FileReader fileReader;
    private File file;
    private Config config;

    public MyFileReader() {
        file = new File(configFileName);
        resetConfig();
        setFile();
    }

    public MyFileReader(String name) {
        file = new File(name);
        resetConfig();
        setFile();
    }

    // always run this after initiating
    public boolean setFile() {
        try {
            fileReader = new FileReader(file);
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    public void readFile() {
        BufferedReader b = new BufferedReader(fileReader);

        String line;
        try {
            while ((line = b.readLine()) != null) {
                parser(line);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void resetConfig() {
        config = new Config();
    }

    private void parser(String s) {
        String[] split;
        try {
            // [0] is attribute, [1] is value
            split = s.split("=", 2);

            switch (split[0]) {
                case "n":
                    config.n = Integer.parseInt(split[1]);
                    break;
                case "ts":
                    config.ts = Integer.parseInt(split[1]);
                    break;
                case "Tp":
                    config.Tp = Double.parseDouble(split[1]);
                    break;
                case "r":
                    config.r = Double.parseDouble(split[1]);
                    break;
                case "timeBetweenSamples":
                    config.timeBetweenSamples = Double.parseDouble(split[1]);
                    break;
                case "nbrOfMeasurements":
                    config.nbrOfMeasurements = Integer.parseInt(split[1]);
                    break;
                case "xs":
                    String[] xs = (split[1]).split(",");
                    int nX = xs.length;
                    config.xs = new Integer[nX];

                    for (int i = 0; i < nX; i++) {
                        config.xs[i] = Integer.parseInt(xs[i]);
                    }

                    break;
                case "ys":
                    String[] ys = (split[1]).split(",");
                    int nY = ys.length;

                    config.ys = new Integer[nY];
                    for (int i = 0; i < nY; i++) {
                        config.ys[i] = Integer.parseInt(ys[i]);
                    }

                    break;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public Config getConfig() {
        return config;
    }
}