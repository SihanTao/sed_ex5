package ic.doc;

public record Transform(String targetFilename) {

  public static Transform transformFrom(String targetFilename) {
    return new Transform(targetFilename);
  }
}
