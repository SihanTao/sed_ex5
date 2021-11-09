package ic.doc;

import static ic.doc.Transform.TransformFrom;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

public class ImageTransformer {

  private final Path downloadDirectory;

  public ImageTransformer(Path downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  public void transform(List<Transform> filesToTransform) throws IOException, InterruptedException {

    long startTime = System.currentTimeMillis();

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    for (Transform toTransform : filesToTransform) {
      executorService.submit(new ImageTransformTask(downloadDirectory, toTransform.targetFilename()));
    }

    executorService.shutdown();
    executorService.awaitTermination(120, TimeUnit.SECONDS);

    long endTime = System.currentTimeMillis();

    System.out.printf("Total runtime: %dms%n", endTime - startTime);
  }

  public static void main(String[] args) throws Exception {

    Path downloadDirectory = Paths.get("").resolve("images");

    List<Transform> imagesToTransform = List.of(
        TransformFrom("stata_centre.jpg"),
        TransformFrom("frog.jpg"),
        TransformFrom("eggs.jpg"),
        TransformFrom("moth.jpg"),
        TransformFrom("trace.jpg")
    );

    new ImageTransformer(downloadDirectory).transform(imagesToTransform);
  }
}
