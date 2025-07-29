package endpoints.synchronisation;

import transport.exceptions.TransportIOException;

/** Interface to group all synchronisation implementations. */
public interface Synchronizer {

  /**
   * Send the given object to the other party. Only one party should ever send, otherwise race
   * conditions could appear.
   *
   * @param obj the object to send
   * @param <T> the type of the object
   */
  <T> void synchronize(T obj);

  /**
   * Registers the specified object for synchronisation. If the class implements the updatable
   * interface, the update method is called on each synchronisation.
   *
   * @param obj the object to sync
   * @param <T> the object class
   */
  <T> void register(T obj);

  /**
   * Registers the specified object for synchronisation. If the class implements the updatable
   * interface, the update method is called on each synchronisation.
   *
   * @param creator the creator for the object
   * @param <T> the object class
   * @throws TransportIOException if the object could not be created
   */
  <T> void register(SynchronizedCreator<T> creator) throws TransportIOException;

  /**
   * Unregister the specified object for synchronisation. The object will no longer be updated.
   *
   * @param obj the object to unregister
   * @param <T> the objects class
   */
  <T> void unregister(T obj);

  /** Clears all registered objects. */
  void clear();

  /**
   * Interface to create an object and handle it.
   *
   * @param <T> the object type
   */
  interface SynchronizedCreator<T> {
    /**
     * Creates a synchronized object.
     *
     * @return the object
     */
    T createObject();

    /**
     * Handle the object.
     *
     * @param obj the object
     */
    void handleObject(T obj);
  }
}
