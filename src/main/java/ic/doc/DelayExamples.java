package ic.doc;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DelayExamples {

  public static void main(String[] args) throws Exception {

    List<Delay> delays = List.of(
            Delay.of(2, SECONDS),
            Delay.of(2, SECONDS),
            Delay.of(2, SECONDS),
            Delay.of(2, SECONDS),
            Delay.of(4, SECONDS),
            Delay.of(4, SECONDS),
            Delay.of(4, SECONDS),
            Delay.of(4, SECONDS)
    );

    ExecutorService executorService = Executors.newFixedThreadPool(8);

    long startTime = System.currentTimeMillis();

    for (Delay delay : delays) {
      executorService.submit(delay);
    }

    executorService.shutdown();
    executorService.awaitTermination(120, SECONDS);
    long endTime = System.currentTimeMillis();

    long elapsedTime = endTime - startTime;
    System.out.printf("Total elapsed time: %dms%n", elapsedTime);
  }
}
