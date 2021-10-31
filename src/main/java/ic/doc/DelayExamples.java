package ic.doc;

import java.util.List;

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

    long startTime = System.currentTimeMillis();

    for (Delay delay : delays) {
      delay.run();
    }

    long endTime = System.currentTimeMillis();

    long elapsedTime = endTime - startTime;
    System.out.printf("Total elapsed time: %dms%n", elapsedTime);
  }
}
