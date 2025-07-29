package events;

/** The type Set car colour event. */
public class SetCarColourEvent {

  /** The Id. */
  public final int id;

  /** The Colour. */
  public final String colour;

  /**
   * Instantiates a new Set car colour event.
   *
   * @param id the id
   * @param colour the colour
   */
  public SetCarColourEvent(int id, String colour) {
    this.id = id;
    this.colour = colour;
  }

  /**
   * Gets car colour.
   *
   * @return the car colour
   */
  public String getCarColour() {
    return colour;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets car colour message.
   *
   * @return the car colour message
   */
  public String getCarColourMessage() {
    return "Player " + id + " has chosen the " + colour + " car.";
  }
}
