package data;

/**
 * A players wpm score.
 *
 * @param name the name of the player
 * @param wpm the wpm score
 */
public record PlayerWPMs(int wpm, String name) {

  @Override
  public String toString() {
    return "Player: " + name + ", wpm: " + wpm;
  }
}
