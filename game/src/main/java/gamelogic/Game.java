package gamelogic;

import java.util.ArrayList;
import java.util.List;

/** The Game class represents a game and implements the Observer pattern. */
public class Game extends Observable {
  private String text;

  /** The players in the game. */
  public List<Player> players = new ArrayList<>();

  private GameState state = new GameState();

  /**
   * Returns the start time of the game.
   *
   * @return The start time of the game.
   */
  public long getStartTime() {
    return startTime;
  }

  private long startTime;

  /** Constructor for the Game class. Initializes the game with a random text. */
  public Game() {}

  /**
   * Starts the game by selecting a random pre-configured text and setting the game state to
   * IN_PROGRESS.
   */
  public void startGame() {
    setState(GameState.IN_PROGRESS);
    startTime = System.currentTimeMillis();
    notifyObservers();
  }

  /** Quits the game and updates the game state to QUIT. */
  public void quitGame() {
    setState(GameState.QUIT);
    notifyObservers();
  }

  /**
   * Sets the game state.
   *
   * @param state The new game state.
   */
  public void setState(String state) {
    this.state.setState(state);
    notifyObservers();
  }

  /**
   * Returns the current game state.
   *
   * @return The current game state.
   */
  public String getState() {
    return state.getState();
  }

  /**
   * Adds a player to the game.
   *
   * @param player The player to be added.
   */
  public void addPlayer(Player player) {
    players.add(player);
    notifyObservers();
  }

  /**
   * Returns the list of players.
   *
   * @return A list of players.
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Removes a player from the game.
   *
   * @param player The player to be removed.
   */
  public void removePlayer(Player player) {
    players.remove(player);
    notifyObservers();
  }

  /**
   * Returns the text that the players need to type.
   *
   * @return The text that the players need to type.
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the text that the players need to type.
   *
   * @param text The text that the players need to type.
   */
  public void setText(String text) {
    this.text = text;
  }
}
