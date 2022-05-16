
public class Sensor extends Proc {
  Coord coord;
  Gateway gateway;
  // mean
  double ts;

  Sensor(Coord coord, Gateway gateway, double ts) {
    this.coord = coord;
    this.gateway = gateway;
    this.ts = ts;
  }

  @Override
  public void TreatSignal(Signal x) {
    switch (x.signalType) {
      case SEND_MESSAGE:
        sendMessage();
        break;
    }
  }

  private void sendMessage() {
    gateway.addNewTransmission(coord);
    SignalList.SendSignal(SEND_MESSAGE, this, time + Statistics.expMean(ts));
  }
}
