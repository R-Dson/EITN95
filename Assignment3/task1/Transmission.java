public class Transmission {
  Coord sender;
  boolean isInterrupted = false;
  double radius;

  Transmission(Coord sender, double radius) {
    this.sender = sender;
    this.radius = radius;
  }

  boolean overlap(Coord other) {
    return sender.distance(other) <= radius;
  }

  @Override
  public String toString() {
    return "Transmission: " + sender + ", isInterrupted: " + isInterrupted;
  }

}
