package serverui.util;

/**
 * The interface for the Observer pattern.
 *
 * @param <T> the type of the observer
 */
public interface Observer<T> {

  /**
   * Update the observer with the new data.
   *
   * @param t the new data
   */
  void update(T t);
}
