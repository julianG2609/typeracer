package gamelogic;

/** Class Game factory for creating a new game. */
public class GameFactory {
  private GameFactory() {}

  /**
   * Creates a new game.
   *
   * @return the game
   */
  public static Game createGame() {
    // TODO add players
    return new Game();
  }
}
