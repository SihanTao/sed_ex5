package ic.doc;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class DownloadTask implements Runnable {

  private final URL url;
  private final Path downloadDirectory;
  private final String filename;

  public DownloadTask(URL url, Path downloadDirectory, String filename) {
    this.url = url;
    this.downloadDirectory = downloadDirectory;
    this.filename = filename;
  }

  @Override
  public void run() {
    System.out.println("Started downloading " + filename);
    try {
      Path filePath = downloadDirectory.resolve(filename);
      Files.copy(url.openStream(), filePath);
    } catch (IOException e) {
      throw new DownloadException(e);
    }
    System.out.println("Finished downloading " + filename);
  }

}
