package events;

import data.PlayerScore;
import java.util.List;
import java.util.stream.Stream;

/** The PlayerScoreEvent class represents an event which contains scores of players. */
public class PlayerScoreEvent {

  /** Default constructor for PlayerScoreEvent. */
  public PlayerScoreEvent() {}

  /**
   * Creates a PlayerScoreEvent with the specified list of player scores.
   *
   * @param playerScores the list of player scores which will be in the event
   */
  public PlayerScoreEvent(List<PlayerScore> playerScores) {
    this.playerScores = playerScores.toArray(new PlayerScore[0]);
  }

  private PlayerScore[] playerScores;

  /**
   * Gets the player scores ordered by their times.
   *
   * @return a list of PlayerScore objects ordered by the time
   */
  public List<PlayerScore> getOrderedScores() {
    return Stream.of(playerScores).sorted((k, v) -> Math.toIntExact(k.time() - v.time())).toList();
  }
}
