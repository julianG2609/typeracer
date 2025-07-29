package server.storage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashMap;

/** The type User storage. */
public class UserStorage {

  private static final UserStorage instance = new UserStorage();

  private UserStorage() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static UserStorage getInstance() {
    return instance;
  }

  private final HashMap<Integer, User> users = new HashMap<>();

  /**
   * Gets user.
   *
   * @param id the id
   * @return the user
   */
  public User getUser(int id) {
    if (!users.containsKey(id)) {
      addUser(id, new User());
    }
    return users.get(id);
  }

  /**
   * Removes a user from the storage.
   *
   * @param id the id of the user to remove
   */
  public void removeUser(int id) {
    users.remove(id);
  }

  /**
   * Username exists boolean.
   *
   * @param id the id
   * @param username the username
   * @return the boolean
   */
  public boolean usernameExists(int id, String username) {
    return users.values().stream()
        .anyMatch(
            user ->
                (user.getUsername() != null && user.getUsername().equals(username))
                    && !(users.get(id) == user));
  }

  /**
   * Checks if a given colour is already in use by another user.
   *
   * @param id the id of the user to check the colour for
   * @param colour the colour to check
   * @return true if the colour is already in use by another user, false otherwise
   */
  public boolean carColourExists(int id, String colour) {
    return users.values().stream()
        .anyMatch(
            user ->
                (user.getColour() != null && user.getColour().equals(colour))
                    && !(users.get(id) == user));
  }

  private void addUser(int id, User user) {
    users.put(id, user);
  }

  /** The type User. */
  public static class User {
    /** Instantiates a new User. */
    protected User() {}

    /**
     * Gets colour.
     *
     * @return the colour
     */
    public String getColour() {
      return colour;
    }

    /**
     * Sets colour.
     *
     * @param colour the colour
     */
    public void setColour(String colour) {
      this.colour = colour;
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
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
      this.username = username;
    }

    private String username, colour;
  }
}
