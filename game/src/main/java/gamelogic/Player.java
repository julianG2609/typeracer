package gamelogic;

import endpoints.synchronisation.SynchronizedId;
import endpoints.synchronisation.Updatable;

/**
 * The Player class represents a player in the game and serves as a base class for different types
 * of players.
 */
public abstract class Player extends Observable implements Updatable {
  @SynchronizedId private final transient int id;
  private String name;
  private String colour;

  /** The words per minute typed by a player. */
  protected int wpm;

  /** The progress of a player. */
  protected float progress;

  /**
   * Constructs a new Player with ID, name, and car colour.
   *
   * @param id the player ID.
   * @param name the player name.
   * @param colour the car colour of the player.
   */
  protected Player(int id, String name, String colour) {
    this.id = id;
    this.name = name;
    this.colour = colour;
  }

  /**
   * Returns the ID of the player.
   *
   * @return the player's ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the name of the player.
   *
   * @return the player's name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the WPM of the player.
   *
   * @return the player's WPM
   */
  public int getWPM() {
    return wpm;
  }

  /**
   * Returns the progress of the player.
   *
   * @return the player's progress
   */
  public float getProgress() {
    return progress;
  }

  /**
   * Returns the car colour of the player.
   *
   * @return the colour
   */
  public String getColour() {
    return colour;
  }

  @Override
  public void update() {
    notifyObservers();
  }

  /**
   * Copies the attributes of this player to another player.
   *
   * @param other The {@link Player} object to which the attributes will be copied.
   */
  public void copy(Player other) {
    other.name = this.name;
    other.colour = this.colour;
    other.wpm = this.wpm;
    other.progress = this.progress;
    other.notifyObservers();
  }

  /**
   * Returns a string representation of the player.
   *
   * @return A string consisting of the player name, colour, ID, and progress.
   */
  @Override
  public String toString() {
    return "name=" + name + ", colour=" + colour + ", id=" + id + ", progress=" + progress;
  }
}
