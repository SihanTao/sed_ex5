package ic.doc;

import java.util.concurrent.Callable;

public class TimedTask implements Callable<Long> {
  private final Runnable runnable;

  public TimedTask(Delay runnable) {
    this.runnable = runnable;
  }

  @Override
  public Long call() throws Exception {
    long start_time = System.currentTimeMillis();
    runnable.run();
    long end_time = System.currentTimeMillis();
    return end_time - start_time;
  }
}
