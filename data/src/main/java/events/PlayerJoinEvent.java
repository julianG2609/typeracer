package events;

/** The type Player join event. */
public class PlayerJoinEvent {

  /** The Id. */
  public final int id;

  /**
   * Instantiates a new Player join event.
   *
   * @param id the id
   */
  public PlayerJoinEvent(int id) {
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
   * Gets join message.
   *
   * @return the join message
   */
  public String getJoinMessage() {
    return "Player " + id + " has joined the game.";
  }
}
