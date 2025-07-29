package endpoints.synchronisation;

/** Interface to notify a object when it is changed by the synchronizer. */
public interface Updatable {

  /** Called whenever the synchronizer updates the class. */
  void update();
}
