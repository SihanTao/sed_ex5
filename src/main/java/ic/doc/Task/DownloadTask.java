package ic.doc.Task;

import ic.doc.CatchingRunnable;
import ic.doc.DownloadException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

public class DownloadTask implements Runnable {

  private final URL url;
  private final Path downloadDirectory;
  private final String filename;
  private final CountDownLatch latch;

  public DownloadTask(URL url, Path downloadDirectory, String filename,
      CountDownLatch latch) {
    super();
    this.url = url;
    this.downloadDirectory = downloadDirectory;
    this.filename = filename;
    this.latch = latch;
  }

  @Override
  public void run() {
    System.out.println("Started downloading " + filename);
    try {
      Path filePath = downloadDirectory.resolve(filename);
      Files.copy(url.openStream(), filePath);
    } catch (IOException e) {
      throw new DownloadException(e);
    } finally {
      latch.countDown();
    }
    System.out.println("Finished downloading " + filename);

  }

}
