package ic.doc;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ic.doc.Download.fetchFrom;

public class ImageDownloader {

  private final Path downloadDirectory;

  public ImageDownloader(Path downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  public void download(List<Download> filesToFetch) throws IOException {

    cleanDownloadDirectory();

    long startTime = System.currentTimeMillis();

    for (Download toDownload : filesToFetch) {
      new DownloadTask(toDownload.url(), downloadDirectory, toDownload.targetFilename()).download();
    }

    long endTime = System.currentTimeMillis();

    System.out.printf("Total runtime: %dms%n", endTime - startTime);
  }

  private void cleanDownloadDirectory() throws IOException {
    boolean downloadDirectoryExists = !downloadDirectory.toFile().mkdir();
    if (downloadDirectoryExists) {
      FileUtils.cleanDirectory(downloadDirectory.toFile());
    }
  }

  public static void main(String[] args) throws Exception {

    Path downloadDirectory = Paths.get("").resolve("images");

    List<Download> imagesToFetch = List.of(
            fetchFrom(new URL("https://upload.wikimedia.org/wikipedia/commons/b/b8/Stata_Center_%2805689p%292.jpg"), "stata_centre.jpg"),
            fetchFrom(new URL("https://upload.wikimedia.org/wikipedia/commons/c/c1/Variegated_golden_frog_%28Mantella_baroni%29_Ranomafana.jpg"), "frog.jpg"),
            fetchFrom(new URL("https://upload.wikimedia.org/wikipedia/commons/d/dd/Eggs_in_basket_2020_G1.jpg"), "eggs.jpg"),
            fetchFrom(new URL("https://upload.wikimedia.org/wikipedia/commons/7/71/Convolvulus_hawk-moth_%28Agrius_convolvuli%29_2.jpg"), "moth.jpg"),
            fetchFrom(new URL("https://upload.wikimedia.org/wikipedia/commons/8/83/Lissajous-Figur_--_2020_--_7766.jpg"), "trace.jpg")
    );

    new ImageDownloader(downloadDirectory).download(imagesToFetch);
  }
}

