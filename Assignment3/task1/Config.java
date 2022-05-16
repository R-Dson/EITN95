public class Config {
    public int n;
    public int ts;
    public double Tp;
    public double r;
    public double timeBetweenSamples;
    public int nbrOfMeasurements;
    public Integer[] xs;
    public Integer[] ys;

    public Config() {
    }

    public Config(int n, int ts, double Tp, double r, double timeBetweenSamples, int nbrOfMeasurements) {
        this.n = n;
        this.ts = ts;
        this.Tp = Tp;
        this.r = r;
        this.timeBetweenSamples = timeBetweenSamples;
        this.nbrOfMeasurements = nbrOfMeasurements;
    }

    public Config(int n, int ts, double Tp, double r, double timeBetweenSamples, int nbrOfMeasurements, Integer[] xs,
            Integer[] ys) {
        this.n = n;
        this.ts = ts;
        this.Tp = Tp;
        this.r = r;
        this.timeBetweenSamples = timeBetweenSamples;
        this.nbrOfMeasurements = nbrOfMeasurements;
        this.xs = xs;
        this.ys = ys;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("n = " + this.n + "\n");
        sb.append("ts = " + this.ts + "\n");
        sb.append("Tp = " + this.Tp + "\n");
        sb.append("r = " + this.r + "\n");
        sb.append("Time between samples = " + this.timeBetweenSamples + "\n");
        sb.append("Nbr of Measurements = " + this.nbrOfMeasurements + "\n");
        sb.append("xs length:  " + this.xs.length + "\n");
        sb.append("ys length:  " + this.ys.length + "\n");
        return sb.toString();
    }
}
