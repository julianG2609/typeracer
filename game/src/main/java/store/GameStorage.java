package store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamelogic.Game;
import gamelogic.Observable;

/** The Game storage. */
public class GameStorage extends Observable {

  private static final GameStorage INSTANCE = new GameStorage();

  private GameStorage() {}

  /**
   * Gets GameStorage instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static synchronized GameStorage getInstance() {
    return INSTANCE;
  }

  private Game game;

  /**
   * Gets game.
   *
   * @return the game
   */
  @SuppressFBWarnings("EI_EXPOSE_REP")
  public Game getGame() {
    return game;
  }

  /**
   * Sets game.
   *
   * @param game the game
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public void setGame(Game game) {
    this.game = game;
    notifyObservers();
  }
}
