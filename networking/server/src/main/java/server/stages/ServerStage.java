package server.stages;

import server.Server;

/** The type Server stage. */
public abstract class ServerStage {
  /** Instantiates a new Server stage. */
  public ServerStage() {}

  /**
   * Start.
   *
   * @param server the server
   */
  public abstract void start(Server server);

  /** Stop. */
  public abstract void stop();
}
