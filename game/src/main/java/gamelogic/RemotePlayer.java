package gamelogic;

/**
 * The RemotePlayer class represents a player that interacts with the game remotely. It extends the
 * {@link Player} class.
 */
public class RemotePlayer extends Player {

  /**
   * Instantiates a new Remote player.
   *
   * @param id the player ID.
   * @param name the player name.
   * @param colour the car colour of the player.
   */
  public RemotePlayer(int id, String name, String colour) {
    super(id, name, colour);
  }

  @Override
  public String toString() {
    return "[Remote Player: " + super.toString() + "]";
  }
}
