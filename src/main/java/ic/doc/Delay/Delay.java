package ic.doc.Delay;

import java.util.concurrent.TimeUnit;

public class Delay implements Runnable {

  private final int duration;
  private final TimeUnit timeUnit;

  private Delay(int duration, TimeUnit timeUnit) {
    this.duration = duration;
    this.timeUnit = timeUnit;
  }

  public static Delay of(int duration, TimeUnit timeUnit) {
    return new Delay(duration, timeUnit);
  }

  public static Delay randomDelay() {
    return Delay.of((int) (Math.random() * 10), TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    try {
      System.out.println(
          String.format("Sleeping for %d %s", duration, timeUnit.toString().toLowerCase()));
      Thread.sleep(timeUnit.toMillis(duration));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
