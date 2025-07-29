package server;

import conversion.Converter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import endpoints.EndpointManager;
import java.io.IOException;
import java.net.Socket;
import transport.exceptions.TransportException;

/** The type Connection handler. */
public class ConnectionHandler {

  private Server server;

  private Boolean allowConnections = false;

  private Converter converter;
  private ConnectionListener listener;

  private final ConnectionThread thread = new ConnectionThread();

  private static ConnectionHandler INSTANCE = null;

  private ConnectionHandler() {}

  /**
   * Destroy the current singleton instance of the connection handler. Used if the server socket
   * changes.
   */
  public static void destroy() {
    INSTANCE.thread.interrupt();
    INSTANCE = null;
  }

  /**
   * Create for connection handler.
   *
   * @param server the server
   * @param converter the converter
   * @param listener the listener
   * @return the connection handler
   * @throws IOException the io exception
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static synchronized ConnectionHandler createFor(
      Server server, Converter converter, ConnectionListener listener) throws IOException {
    if (INSTANCE == null) {
      ConnectionHandler connectionHandler = new ConnectionHandler();
      connectionHandler.server = server;
      connectionHandler.listener = listener;
      connectionHandler.converter = converter;
      connectionHandler.thread.start();
      INSTANCE = connectionHandler;
      return connectionHandler;
    } else {
      INSTANCE.listener = listener;
      return INSTANCE;
    }
  }

  /**
   * Is allow connections boolean.
   *
   * @return the boolean
   */
  public boolean isAllowConnections() {
    return allowConnections;
  }

  /**
   * Sets allow connections.
   *
   * @param allowConnections the allow connections
   */
  public void setAllowConnections(boolean allowConnections) {
    this.allowConnections = allowConnections;
    synchronized (thread) {
      thread.notify();
    }
  }

  private class ConnectionThread extends Thread {
    @Override
    @SuppressFBWarnings("UW_UNCOND_WAIT")
    public void run() {
      interrupted();
      while (!isInterrupted()) {
        try {
          Socket client = server.socketAccept();
          while (!allowConnections) {
            synchronized (thread) {
              thread.wait();
            }
          }
          int id = (client.getInetAddress().getHostAddress() + client.getPort()).hashCode();
          EndpointManager manager = new EndpointManager(converter, client);
          manager.connect();
          listener.handle(id, manager);
        } catch (IOException ignored) {
        } catch (TransportException e) {
          // Unreachable
        } catch (InterruptedException e) {
          // Quit
        }
      }
    }
  }

  /** The interface Connection listener. */
  public interface ConnectionListener {
    /**
     * Handle.
     *
     * @param id the id
     * @param manager the manager
     */
    void handle(int id, EndpointManager manager);
  }
}
