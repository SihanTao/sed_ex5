package ic.doc;

import static ic.doc.TransformTask.TRANSFORMED_FILE_SUFFIX;
import static ic.doc.Transform.TransformFrom;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class ImageTransformer {

  private final Path downloadDirectory;

  public ImageTransformer(Path downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  private static void cleanTransformedDirectory(Path path) {
    File dir = new File(path.toString());
    FileFilter fileFilter = new WildcardFileFilter("*" + TRANSFORMED_FILE_SUFFIX,
        IOCase.INSENSITIVE);
    File[] fileList = dir.listFiles(fileFilter);

    if (fileList != null) {
      for (File file : fileList) {
        file.delete();
      }
    }
  }

  public static void main(String[] args) throws Exception {

    Path downloadDirectory = Paths.get("").resolve("images");

    cleanTransformedDirectory(downloadDirectory);
    File dir = new File(downloadDirectory.toString());
    FileFilter fileFilter = new WildcardFileFilter("*.JPG", IOCase.INSENSITIVE);
    File[] fileList = dir.listFiles(fileFilter);

    List<Transform> imagesToTransform = new ArrayList<>();
    if (fileList != null) {
      for (File file : fileList) {
        imagesToTransform.add(TransformFrom(file.getName()));
      }
    }

    new ImageTransformer(downloadDirectory).transform(imagesToTransform);
  }

  public void transform(List<Transform> filesToTransform)
      throws IOException, InterruptedException, ExecutionException {

    cleanTransformedDirectory(downloadDirectory);

    long startTime = System.currentTimeMillis();

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    List<Future<Long>> futures = new ArrayList<>();
    Map<Future<Long>, String> futureFilenameHashMap = new HashMap<>();

    CountDownLatch latch = new CountDownLatch(filesToTransform.size());
    for (Transform toTransform : filesToTransform) {
      Future<Long> future = executorService.submit(
          new TimedTask(new TransformTask(downloadDirectory, toTransform.targetFilename(), latch)));
      futures.add(future);
      futureFilenameHashMap.put(future, toTransform.targetFilename());
    }

    latch.await();
    executorService.shutdown();

    for (Future<Long> future : futures) {
      String filename = futureFilenameHashMap.get(future);
      System.out.printf("%s processing Time: %dms%n", filename, future.get());
    }

    executorService.awaitTermination(120, TimeUnit.SECONDS);

    long endTime = System.currentTimeMillis();

    System.out.printf("Total runtime: %dms%n", endTime - startTime);
  }
}
