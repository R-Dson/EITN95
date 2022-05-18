
public class Sensor extends Proc {
  Coord coord;
  Gateway gateway;
  // mean
  double ts;
  boolean isSmart;
  double lb;
  double ub;

  Sensor(Coord coord, Gateway gateway, double ts, boolean isSmart, double lb, double ub) {
    this.coord = coord;
    this.gateway = gateway;
    this.ts = ts;
    this.isSmart = isSmart;
    this.lb = lb;
    this.ub = ub;
  }

  @Override
  public void TreatSignal(Signal x) {
    switch (x.signalType) {
      case SEND_MESSAGE:
        if (isSmart)
          smartSendMessage();
        else
          sendMessage();
        break;
    }
  }

  private void smartSendMessage() {
    boolean isWithinRange = gateway.senseForOtherSenders(coord);
    if (isWithinRange) {
      SignalList.SendSignal(SEND_MESSAGE, this, time + Statistics.uniformDistribution(lb, ub));
    } else {
      sendMessage();
    }
  }

  private void sendMessage() {
    gateway.addNewTransmission(coord);
    SignalList.SendSignal(SEND_MESSAGE, this, time + Statistics.expMean(ts));
  }
}
