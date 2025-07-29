package events;

/** The type Text event. */
public class TextEvent {
  /**
   * Gets text.
   *
   * @return the text
   */
  public String getText() {
    return text;
  }

  private final String text;

  /**
   * Instantiates a new Text event.
   *
   * @param text the text
   */
  public TextEvent(String text) {
    this.text = text;
  }

  /** Instantiates a new Text event. */
  public TextEvent() {
    text = "";
  }
}
