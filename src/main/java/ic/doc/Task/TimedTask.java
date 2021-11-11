package ic.doc.Task;

import java.util.concurrent.Callable;

public class TimedTask implements Callable<Long> {

  private final Runnable runnable;

  public TimedTask(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public Long call() {
    long start_time = System.currentTimeMillis();
    try {
      runnable.run();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      throw e;
    }
    long end_time = System.currentTimeMillis();
    return end_time - start_time;
  }
}
