package gamelogic;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Observable class is a base class for objects that want to notify their observers about
 * changes in their state. It follows the Observer pattern.
 */
public class Observable {
  private final transient List<Observer> observers = new CopyOnWriteArrayList<>();

  /**
   * Default constructor for the Observable class. Initializes a new instance of the Observable
   * class.
   */
  public Observable() {}

  /**
   * Adds an observer to the list of observers and immediately updates the observer.
   *
   * @param observer The {@link Observer} to be added.
   */
  public synchronized void addObserver(Observer observer) {
    observers.add(observer);
    observer.update();
  }

  /**
   * Removes an observer from the list of observers.
   *
   * @param observer The {@link Observer} to be removed.
   */
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  /** Notifies all registered observers of a change by calling their update method. */
  protected synchronized void notifyObservers() {
    for (Observer observer : observers) {
      observer.update();
    }
  }
}
