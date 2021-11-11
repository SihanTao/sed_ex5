package ic.doc;

import java.net.URL;

public record Download(URL url, String targetFilename) {

  public static Download fetchFrom(URL url, String targetFilename) {
    return new Download(url, targetFilename);
  }
}

