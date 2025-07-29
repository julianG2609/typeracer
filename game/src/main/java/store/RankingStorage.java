package store;

import data.PlayerScore;
import data.PlayerWPMs;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamelogic.Observable;
import java.util.*;

/** The Ranking storage. */
public class RankingStorage extends Observable {

  static final RankingStorage INSTANCE = new RankingStorage();

  private RankingStorage() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static synchronized RankingStorage getInstance() {
    return INSTANCE;
  }

  private List<PlayerWPMs> wpmRanking;

  private List<PlayerScore> overallRanking;

  /**
   * Gets wpm ranking.
   *
   * @return the wpm ranking
   */
  public List<PlayerWPMs> getWpmRanking() {
    if (wpmRanking == null) {
      return null;
    }
    return Collections.unmodifiableList(wpmRanking);
  }

  /**
   * Gets WPM for a specific player.
   *
   * @param playerName the player name
   * @return the wpm score
   */
  public int getWPMfor(String playerName) {
    for (PlayerWPMs wpm : wpmRanking) {
      System.out.println(wpm);
      if (Objects.equals(wpm.name(), playerName)) {
        return wpm.wpm();
      }
    }
    return 0;
  }

  /**
   * Sets wpm ranking.
   *
   * @param wpmRanking the wpm ranking
   */
  public void setWpmRanking(List<PlayerWPMs> wpmRanking) {
    this.wpmRanking = Collections.unmodifiableList(wpmRanking);
  }

  /**
   * Gets overall ranking.
   *
   * @return the overall ranking
   */
  public List<PlayerScore> getOverallRanking() {
    if (overallRanking == null) {
      return null;
    }
    return Collections.unmodifiableList(overallRanking);
  }

  /**
   * Sets overall ranking.
   *
   * @param overallRanking the overall ranking
   */
  public void setOverallRanking(List<PlayerScore> overallRanking) {
    this.overallRanking = Collections.unmodifiableList(overallRanking);
    notifyObservers();
  }

  /** Resets the rankings. */
  public void resetRankings() {
    wpmRanking = null;
    overallRanking = null;
  }
}
