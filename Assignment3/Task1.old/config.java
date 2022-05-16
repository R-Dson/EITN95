public class config {
    public int n;
    public int ts;
    public double Tp;
    public double r;
    public Integer[] x;
    public Integer[] y;

    public config () { }

    public config (int n, int ts, double Tp, double r)
    {
        this.n = n;
        this.ts = ts;
        this.Tp = Tp;
        this.r = r;
    }

    public config (int n, int ts, double Tp, double r, Integer[] x, Integer[] y)
    {
        this.n = n;
        this.ts = ts;
        this.Tp = Tp;
        this.r = r;
        this.x = x;
        this.y = y;
    }
    
}
