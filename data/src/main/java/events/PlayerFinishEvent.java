package events;

/** The type Player finish event. */
public class PlayerFinishEvent {

  /** The Id. */
  public final int id;

  /**
   * Instantiates a new Player finish event.
   *
   * @param id the id
   */
  public PlayerFinishEvent(int id) {
    this.id = id;
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
   * Gets finish message.
   *
   * @return the finish message
   */
  public String getFinishMessage() {
    return "Player " + id + " has finished the game.";
  }
}
