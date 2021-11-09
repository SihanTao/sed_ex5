package ic.doc;

import com.jhlabs.image.KaleidoscopeFilter;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class TransformTask implements Runnable {

  public static final String TRANSFORMED_FILE_SUFFIX = "-filtered-small.jpg";
  private final Path directory;
  private final String filename;

  public TransformTask(Path directory, String filename) {
    this.directory = directory;
    this.filename = filename;
  }

  @Override
  public void run() {
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
