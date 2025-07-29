package events;

/** The type Ready event. */
public class ReadyEvent {
  private final boolean ready;

  private final int id;

  /**
   * Instantiates a new Ready event.
   *
   * @param ready the ready
   * @param id the id
   */
  public ReadyEvent(boolean ready, int id) {
    this.ready = ready;
    this.id = id;
  }

  /**
   * Returns whether player is ready or not.
   *
   * @return the boolean
   */
  public boolean isReady() {
    return ready;
  }

  /**
   * Returns the id of player.
   *
   * @return the id of player
   */
  public int getId() {
    return id;
  }
}
