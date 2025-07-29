package events;

/** The type Username taken event. */
public class UsernameTakenEvent {
  /** The Id. */
  public final int id;

  /** Whether username is taken or not. */
  public final boolean success;

  /**
   * Instantiates a new Username not taken event.
   *
   * @param id the id
   * @param success indicator whether username is taken or not
   */
  public UsernameTakenEvent(int id, boolean success) {
    this.id = id;
    this.success = success;
  }

  /**
   * Returns whether username is taken or not.
   *
   * @return whether username is taken or not
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
   * Gets username message.
   *
   * @return the username message
   */
  public String getUsernameTakenMessage() {
    if (success) return "Player " + id + "'s name is not taken!";
    else return "Player " + id + "'s name is already taken!";
  }
}
