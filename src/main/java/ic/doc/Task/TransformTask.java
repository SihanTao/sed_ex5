package ic.doc.Task;

import com.jhlabs.image.KaleidoscopeFilter;
import ic.doc.ImageTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class TransformTask implements Runnable {

  public static final String TRANSFORMED_FILE_SUFFIX = "-filtered-small.jpg";
  private final Path directory;
  private final String filename;
  private final CountDownLatch downloadLatch;

  public TransformTask(Path directory, String filename, CountDownLatch downloadLatch) {
    this.directory = directory;
    this.filename = filename;
    this.downloadLatch = downloadLatch;
  }

  public TransformTask(Path directory, String filename) {
    this.directory = directory;
    this.filename = filename;
    this.downloadLatch = null;
  }

  @Override
  public void run() {
    if (downloadLatch != null) {
      try {
        downloadLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("Started transforming " + filename);
    try {

      BufferedImage inputImage = ImageIO.read(directory.resolve(filename).toFile());

      KaleidoscopeFilter filter = new KaleidoscopeFilter();
      filter.setSides(10);

      BufferedImage filtered = filter.filter(inputImage, inputImage);
      BufferedImage resized = Scalr.resize(filtered, 400);

      String newFileName = filename.replace(".jpg", TRANSFORMED_FILE_SUFFIX);

      ImageIO.write(resized, "jpg", directory.resolve(newFileName).toFile());

    } catch (IOException e) {
      throw new ImageTransformException(e);
    }

    System.out.println("Finished transforming " + filename);
  }
}
