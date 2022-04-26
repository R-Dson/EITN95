public class Config {
    public int N;
    public int x;
    public double lambda;
    public int T;
    public int M;

    Config setN(int N) {
        this.N = N;
        return this;
    }

    Config setx(int x) {
        this.x = x;
        return this;
    }

    Config setlambda(double lambda) {
        this.lambda = lambda;
        return this;
    }

    Config setT(int T) {
        this.T = T;
        return this;
    }

    Config setM(int M) {
        this.M = M;
        return this;
    }

    static Config defaultConfig() {
        Config config = new Config();
        return config
                .setN(1000)
                .setx(100)
                .setlambda(8.0)
                .setT(1)
                .setM(1000);
    }
}
