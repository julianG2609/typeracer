package model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamelogic.Observable;

/**
 * The Start game model represents the model for starting a game and extends the Observable class to
 * allow observers to be notified of changes in the game state.
 */
public class StartGameModel extends Observable {

  private static StartGameModel instance = new StartGameModel();

  /**
   * Gets instance of the StartGameModel.
   *
   * @return the instance of the StartGameModel
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static StartGameModel getInstance() {
    return instance;
  }

  /**
   * Resets the instance of the StartGameModel.
   *
   * @return the new instance of StartGameModel
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static StartGameModel resetInstance() {
    instance = new StartGameModel();
    return instance;
  }

  /** Instantiates a new StartGameModel. */
  private StartGameModel() {}

  private boolean ready = false;

  private boolean usernametaken = false;

  private boolean usernameEmpty = false;

  private boolean carColourTaken = false;

  /** Flips the ready value and notifies observers about the change in the game state. */
  public void flip() {
    ready = !ready;
    // ReadyEvent event = new ReadyEvent(ready);
    notifyObservers();
    // TODO send
  }

  /**
   * Checks whether someone is ready or not.
   *
   * @return {@code true} if ready, {@code false} if not ready
   */
  public boolean isReady() {
    return ready;
  }

  /**
   * Checks if the username is taken.
   *
   * @return {@code true} if username is taken, {@code false} if not taken
   */
  public boolean isUsernametaken() {
    return usernametaken;
  }

  /**
   * Checks if the username is empty.
   *
   * @return {@code true} if username is empty, {@code false} if not empty
   */
  public boolean isUsernameEmpty() {
    return usernameEmpty;
  }

  /**
   * Checks if the car colour is taken.
   *
   * @return {@code true} if car colour is taken, {@code false} if not taken
   */
  public boolean isCarColourTaken() {
    return carColourTaken;
  }

  /**
   * Sets the username taken status. Notifies observers about the change in the status of username.
   *
   * @param usernametaken the status which indicates if the username is taken
   */
  public void setUsernametaken(boolean usernametaken) {
    this.usernametaken = usernametaken;
    notifyObservers();
  }

  /**
   * Sets the username empty status. Notifies observers about the change in the status of username.
   *
   * @param usernameEmpty the status which indicates if the username is empty
   */
  public void setUsernameEmpty(boolean usernameEmpty) {
    this.usernameEmpty = usernameEmpty;
    notifyObservers();
  }

  /**
   * Sets the car colour taken status. Notifies observers about the change in the status of car
   * colour.
   *
   * @param carColourTaken the status which indicates if the car colour is taken
   */
  public void setCarColourTaken(boolean carColourTaken) {
    this.carColourTaken = carColourTaken;
    notifyObservers();
  }
}
