
public class Sensor extends Proc {
  Coord coord;
  Gateway gateway;
  // mean
  double ts;
  boolean isSmart = true;
  double lb;
  double ub;

  Sensor(Coord coord, Gateway gateway, double ts) {
    this.coord = coord;
    this.gateway = gateway;
    this.ts = ts;
    this.lb = ts * 0.1;
    this.ub = ts * 0.5;
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
