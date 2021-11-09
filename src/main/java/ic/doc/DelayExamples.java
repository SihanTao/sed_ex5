package ic.doc;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    long startTime = System.currentTimeMillis();

    List<Future<Long>> futures = new ArrayList<>();
    for (Delay delay : delays) {
      futures.add(executorService.submit(new TimedTask(delay)));
    }

    executorService.shutdown();

    long totalProcessingTime = 0;
    for (Future<Long> future : futures) {
      totalProcessingTime += future.get();
    }
    executorService.awaitTermination(120, SECONDS);
    long endTime = System.currentTimeMillis();

    long elapsedTime = endTime - startTime;
    System.out.printf("Total processing time: %dms%n", totalProcessingTime);
    System.out.printf("Total elapsed time: %dms%n", elapsedTime);
  }
}
