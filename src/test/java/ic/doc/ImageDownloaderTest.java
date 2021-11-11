package ic.doc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import ic.doc.Task.TransformTask;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class ImageDownloaderTest {

  @Test
  public void downloadsNamedFiles() throws Exception {

    Path downloadDir = Files.createTempDirectory("emptyDir");
    downloadDir.toFile().deleteOnExit();

    List<Download> images = List.of(
        new Download(new URL("https://upload.wikimedia.org/wikipedia/commons/c/c1/"
            + "Variegated_golden_frog_%28Mantella_baroni%29_Ranomafana.jpg"),
            "frog.jpg"),
        new Download(new URL("https://upload.wikimedia.org/wikipedia/commons/8/83/"
            + "Lissajous-Figur_--_2020_--_7766.jpg"),
            "trace.jpg"));

    new ImageDownloader(downloadDir).download(images);

    Set<String> expectedNames =
        images.stream()
            .map(Download::targetFilename)
            .collect(Collectors.toSet());

    Set<String> downloadedNames =
        Set.of(downloadDir.toFile().list(
            (dir, name) -> !name.endsWith(TransformTask.TRANSFORMED_FILE_SUFFIX)));

    assertThat(downloadedNames, is(equalTo(expectedNames)));
  }

}
