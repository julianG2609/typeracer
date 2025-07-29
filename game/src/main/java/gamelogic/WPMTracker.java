package gamelogic;

import store.GameStorage;

/** The WPMTracker class is responsible for tracking and calculating the WPM typed by a player. */
public class WPMTracker {

  /** Constructs a new WPMTracker. */
  public WPMTracker() {}

  /**
   * Calculates the WPM of a player based on the number of words typed and the elapsed time since
   * the game started.
   *
   * @param wordsTyped the number of words typed by the user.
   * @return the calculated WPM, or 0 if the elapsed time is zero.
   */
  public int calculateWPM(int wordsTyped) {
    long currentTime = System.currentTimeMillis();
    long elapsedTimeMillis = currentTime - GameStorage.getInstance().getGame().getStartTime();
    double elapsedTimeMinutes = elapsedTimeMillis / 60000.0;
    int wpm = (elapsedTimeMinutes > 0) ? (int) (wordsTyped / elapsedTimeMinutes) : 0;
    return wpm;
  }
}
