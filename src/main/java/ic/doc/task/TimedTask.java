package ic.doc.task;

import java.util.concurrent.Callable;

public class TimedTask implements Callable<Long> {

  private final Runnable runnable;

  public TimedTask(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public Long call() {
    long startTime = System.currentTimeMillis();
    try {
      runnable.run();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      throw e;
    }
    long endTime = System.currentTimeMillis();
    return endTime - startTime;
  }
}
