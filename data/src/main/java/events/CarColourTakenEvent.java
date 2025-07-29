package events;

/** The type Car colour taken event. */
public class CarColourTakenEvent {

  /** The Id. */
  public final int id;

  /** Whether car colour is taken or not. */
  public final boolean success;

  /**
   * Instantiates a new Car colour taken event.
   *
   * @param id the id
   * @param success indicator whether car colour is taken or not
   */
  public CarColourTakenEvent(int id, boolean success) {
    this.id = id;
    this.success = success;
  }

  /**
   * Returns whether car colour is taken or not.
   *
   * @return whether car colour is taken or not
   */
  public boolean isSuccess() {
    return success;
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
   * Gets car colour taken message.
   *
   * @return the car colour taken message
   */
  public String getCarColourTakenMessage() {
    if (success) return "Player " + id + "'s car colour is not taken!";
    else return "Player " + id + "'s car colour is already taken!";
  }
}
