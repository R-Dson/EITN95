import java.util.ArrayList;

public class Gateway extends Proc {
  int nbrOfTransmissions = 0;
  int nbrOfSuccess = 0;
  ArrayList<Double> Tputs = new ArrayList<>();

  ArrayList<Double> PLosses = new ArrayList<>();

  double radius;
  double Tp;
  double timeBetweenSamples;

  // if in this list when new then always overlap in time.
  ArrayList<Transmission> transmissions = new ArrayList<>();

  public Gateway(double radius, double timeBetweenSamples, double Tp) {
    this.radius = radius;
    this.timeBetweenSamples = timeBetweenSamples;
    this.Tp = Tp;
  }

  @Override
  public void TreatSignal(Signal x) {
    switch (x.signalType) {
      case END_OF_ONE_TRANSMISSION:
        endOfTransmission();
        break;
      case MEASURE:
        measure();
        break;
    }
  }

  // FIFO
  void addNewTransmission(Coord newCoord) {
    nbrOfTransmissions++;
    Transmission latest = new Transmission(newCoord, radius);

    for (Transmission transmission : transmissions) {
      latest.isInterrupted = true;
      transmission.isInterrupted = true;
    }
    transmissions.add(latest);
    SignalList.SendSignal(END_OF_ONE_TRANSMISSION, this, time + Tp);
  }

  // FIFO
  boolean senseForOtherSenders(Coord newCoord) {

    boolean isWithinRange = false;
    for (Transmission transmission : transmissions) {
      // check if within listening range
      if (transmission.overlap(newCoord))
        isWithinRange = true;
    }

    return isWithinRange;
  }

  private void endOfTransmission() {
    Transmission transmission = transmissions.remove(0);
    if (!transmission.isInterrupted)
      nbrOfSuccess++;
  }

  private void measure() {
    Tputs.add((double) nbrOfSuccess / time);
    double denom = (double) nbrOfTransmissions - nbrOfSuccess;
    double res = denom / (double) nbrOfTransmissions;
    PLosses.add(res);
    SignalList.SendSignal(MEASURE, this, time + timeBetweenSamples);
  }
}
