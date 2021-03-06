package ic.doc;

import static ic.doc.Download.fetchFrom;

import ic.doc.task.DownloadTask;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

public class ImageDownloader {

  private final Path downloadDirectory;

  public ImageDownloader(Path downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  public static void main(String[] args) throws Exception {

    Path downloadDirectory = Paths.get("").resolve("images");

    List<Download> imagesToFetch = List.of(
        fetchFrom(new URL(
                "https://upload.wikimedia.org/wikipedia/commons/b/b8/Stata_Center_%2805689p%292.jpg"),
            "stata_centre.jpg"),
        fetchFrom(new URL(
                "https://upload.wikimedia.org/wikipedia/commons/c/c1/Variegated_golden_frog_%28Mantella_baroni%29_Ranomafana.jpg"),
            "frog.jpg"),
        fetchFrom(new URL(
                "https://upload.wikimedia.org/wikipedia/commons/d/dd/Eggs_in_basket_2020_G1.jpg"),
            "eggs.jpg"),
        fetchFrom(new URL(
                "https://upload.wikimedia.org/wikipedia/commons/7/71/Convolvulus_hawk-moth_%28Agrius_convolvuli%29_2.jpg"),
            "moth.jpg"),
        fetchFrom(new URL(
                "https://upload.wikimedia.org/wikipedia/commons/8/83/Lissajous-Figur_--_2020_--_7766.jpg"),
            "trace.jpg"),
        fetchFrom(new URL(
                "http://http://nosuchhost.doc.ic.ac.uk/not-here.jpg"),
            "exception.jpg")
    );

    new ImageDownloader(downloadDirectory).download(imagesToFetch);
  }

  public void download(List<Download> filesToFetch) throws IOException, InterruptedException {

    cleanDownloadDirectory();

    final long startTime = System.currentTimeMillis();

    CountDownLatch latch = new CountDownLatch(filesToFetch.size());

    ExecutorService executorService = Executors.newFixedThreadPool(5);

    for (Download toDownload : filesToFetch) {
      CatchingRunnable catchingRunnable = new CatchingRunnable(
          new DownloadTask(toDownload.url(), downloadDirectory, toDownload.targetFilename(),
              latch));
      executorService
          .submit(catchingRunnable);
    }

    executorService.shutdown();
    try {
      latch.await();
      executorService.awaitTermination(120, TimeUnit.SECONDS);
    } catch (DownloadException e) {
      System.out.println(e.getMessage());
      throw e;
    }

    final long endTime = System.currentTimeMillis();

    System.out.printf("Total runtime: %dms%n", endTime - startTime);
  }

  private void cleanDownloadDirectory() throws IOException {
    boolean downloadDirectoryExists = !downloadDirectory.toFile().mkdir();
    if (downloadDirectoryExists) {
      FileUtils.cleanDirectory(downloadDirectory.toFile());
    }
  }
}

