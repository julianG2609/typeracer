package endpoints;

import conversion.Converter;

/**
 * Abstract class to group networking functionalities. Each endpoint should provide a unique
 * service. Can be used with an EndpointManager to bind it to a connection.
 */
public abstract class Endpoint {

  /** The default converter of the connected endpoint manager. */
  protected Converter converter;

  /** The id of this endpoint. Given by the manager. */
  protected byte id;

  /** The associated Endpoint manager. */
  protected volatile EndpointManager endpointManager;

  /** Default constructor. */
  public Endpoint() {}

  /**
   * Handles received data. Called by the Endpoint Manager.
   *
   * @param data the received data
   */
  protected abstract void handleReceived(byte[] data);

  /**
   * Sends the specified data.
   *
   * @param data the data
   */
  protected void send(byte[] data) {
    endpointManager.send(id, data);
  }

  /**
   * Handles the registration event. The endpoint should only work, after this is called. Before
   * this method is called, no data can be sent or received.
   */
  protected abstract void registeredHandler();

  /** Handles the deregistration event. */
  public abstract void destroy();
}
