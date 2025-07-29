package store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** The User storage. */
public class UserStorage {
  private UserStorage() {}

  static final UserStorage INSTANCE = new UserStorage();

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static synchronized UserStorage getInstance() {
    return INSTANCE;
  }

  private String name;
  private String colour;

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

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
}
