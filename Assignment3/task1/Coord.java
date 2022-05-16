
public class Coord {
  int x;
  int y;

  public Coord(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public double distance(Coord other) {
    return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
  }

  @Override
  public String toString() {
    return "Coord(" + x + ", " + y + ")";
  }
}
