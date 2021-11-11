package ic.doc;

public class CatchingRunnable implements Runnable {

  private final Runnable delegate;

  public CatchingRunnable(Runnable delegate) {
    this.delegate = delegate;
  }

  @Override
  public void run() {
    try {
      delegate.run();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }
}
