package events;

/** The type Player leave event. */
public class PlayerLeaveEvent {

  /** The Id. */
  public final int id;

  /**
   * Instantiates a new Player leave event.
   *
   * @param id the id
   */
  public PlayerLeaveEvent(int id) {
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
   * Gets left message.
   *
   * @return the left message
   */
  public String getLeftMessage() {
    return "Player " + id + " has left the game.";
  }
}
