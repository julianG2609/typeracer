package events;

/** The type Set username event. */
public class SetUsernameEvent {

  /** The Id. */
  public final int id;

  /** The Username. */
  public final String username;

  /**
   * Instantiates a new Set username event.
   *
   * @param id the id
   * @param username the username
   */
  public SetUsernameEvent(int id, String username) {
    this.id = id;
    this.username = username;
  }

  /**
   * Gets username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
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
  public String getUsernameMessage() {
    return "Player " + id + " has set their username to " + getUsername();
  }
}
