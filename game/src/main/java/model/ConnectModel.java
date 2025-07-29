package model;

import gamelogic.Observable;

/**
 * The ConnectModel class extends the {@link Observable} class and represents the model in the MVC
 * pattern for managing the connection state in the game.
 */
public class ConnectModel extends Observable {

  /** Instantiates a new ConnectModel. */
  public ConnectModel() {}

  /**
   * Is error boolean.
   *
   * @return the boolean.
   */
  public boolean isError() {
    return error;
  }

  private boolean error = false;

  /**
   * Sets the error state and notifies observers of the change.
   *
   * @param error the error state to set.
   */
  public void showError(boolean error) {
    this.error = error;
    notifyObservers();
  }
}
