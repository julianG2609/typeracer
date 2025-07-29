package data;

/**
 * The PlayerScore record is the score of a player in the game. It contains the player's name and
 * their time.
 *
 * @param time the time a player needed
 * @param name the name of player
 */
public record PlayerScore(long time, String name) {

  @Override
  public String toString() {
    return "Player: " + name + ", time: " + time;
  }
}
