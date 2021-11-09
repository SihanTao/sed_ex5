package ic.doc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TimedTask implements Callable<Long> {
  private final Delay delay;

  public TimedTask(Delay delay) {
    this.delay = delay;
  }

  @Override
  public Long call() throws Exception {
    long start_time = System.currentTimeMillis();
    delay.run();
    long end_time = System.currentTimeMillis();
    return end_time - start_time;
  }
}
