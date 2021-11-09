package ic.doc;

import static ic.doc.Transform.TransformFrom;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class ImageTransformer {

  private final Path downloadDirectory;

  public ImageTransformer(Path downloadDirectory) {
    this.downloadDirectory = downloadDirectory;
  }

  public void transform(List<Transform> filesToTransform) throws IOException, InterruptedException {

    long startTime = System.currentTimeMillis();

    for (Transform toTransform : filesToTransform) {
      new ImageTransformTask(downloadDirectory, toTransform.targetFilename()).transform();
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
