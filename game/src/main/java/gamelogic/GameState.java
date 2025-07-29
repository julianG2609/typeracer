package gamelogic;

/**
 * The GameState class represents the current state of the game. It encapsulates the state
 * information and provides methods to access and modify the state.
 */
public class GameState {

  /** Represents the state when the game is waiting to start. */
  public static final String WAITING = "WAITING";

  /** Represents the state when the game is currently in progress. */
  public static final String IN_PROGRESS = "IN_PROGRESS";

  /** Represents the state when the game has finished. */
  public static final String FINISHED = "FINISHED";

  /** Represents the state when the game has been quit by a player. */
  public static final String QUIT = "QUIT";

  /**
   * The default state of the game. Initialized to {@code WAITING}, indicating that the game is
   * waiting to start.
   */
  private String currentState = WAITING;

  /** Default constructor for the GameState class. */
  public GameState() {}

  /**
   * Sets the current state of the game.
   *
   * @param state the new state of the game as a {@link String}.
   */
  public void setState(String state) {
    this.currentState = state;
  }

  /**
   * Returns the current state of the game.
   *
   * @return the current state of the game as a {@link String}.
   */
  public String getState() {
    return currentState;
  }
}
