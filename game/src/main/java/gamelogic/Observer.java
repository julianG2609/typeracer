package gamelogic;

/**
 * The Observer interface should be implemented by any class that wishes to be notified of changes
 * in an Observable object. It follows the Observer pattern.
 */
public interface Observer {

  /** This method is called to notify the observer of a change in the observable object. */
  void update();
}
