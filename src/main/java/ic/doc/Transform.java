package ic.doc;

public record Transform(String targetFilename) {
  public static Transform TransformFrom(String targetFilename) {
    return new Transform(targetFilename);
  }
}
