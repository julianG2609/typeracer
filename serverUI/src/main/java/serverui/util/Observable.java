package serverui.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable class to represent an observable object.
 *
 * @param <T> the type of the observable object
 */
public class Observable<T> {

  /** Instantiates a new Observable. */
  public Observable() {}

  private final List<Observer<T>> observers = new ArrayList<>();

  /**
   * Add observer.
   *
   * @param observer the observer to add
   */
  @SuppressWarnings("unchecked")
  public void addObserver(Observer<T> observer) {
    observers.add(observer);
    observer.update((T) this);
  }

  /**
   * Remove observer.
   *
   * @param observer the observer to remove
   */
  public void removeObserver(Observer<T> observer) {
    observers.remove(observer);
  }

  /** Notify subscribed observers. */
  @SuppressWarnings("unchecked")
  protected void notifyObservers() {
    for (Observer<T> observer : observers) {
      observer.update((T) this);
    }
  }
}
