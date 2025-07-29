package store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** The Settings storage. */
public class SettingsStorage {

  static final SettingsStorage INSTANCE = new SettingsStorage();

  private SettingsStorage() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static synchronized SettingsStorage getInstance() {
    return INSTANCE;
  }

  private boolean darkmode;

  /**
   * Returns whether darkmode is turned on or not.
   *
   * @return the boolean
   */
  public boolean isDarkmode() {
    return darkmode;
  }

  /**
   * Sets darkmode.
   *
   * @param darkmode the darkmode
   */
  public void setDarkmode(boolean darkmode) {
    this.darkmode = darkmode;
  }
}
