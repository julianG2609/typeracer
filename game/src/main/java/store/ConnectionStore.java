package store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gamelogic.EventHandler;
import networking.Client;

/** The type Connection store. */
public class ConnectionStore {

  /** The single instance of ConnectionStore. */
  private static final ConnectionStore INSTANCE = new ConnectionStore();

  /** Private constructor. */
  private ConnectionStore() {}

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static ConnectionStore getInstance() {
    return INSTANCE;
  }

  /**
   * Gets the network client.
   *
   * @return The {@link Client} instance.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP")
  public Client getClient() {
    return client;
  }

  /** The Client. */
  Client client = new Client(new EventHandler());
}
