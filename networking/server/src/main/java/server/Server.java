package server;

import endpoints.EndpointManager;
import endpoints.messaging.MessageEndpoint;
import endpoints.messaging.MessageHandle;
import endpoints.synchronisation.SynchronisationEndpoint;
import events.PlayerJoinEvent;
import events.PlayerLeaveEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.stages.ConnectionStage;
import server.stages.GameStage;
import server.stages.ServerStage;
import server.storage.UserStorage;
import textgeneration.AITextGenerator;

/** The type Server. */
public class Server {
  /**
   * Instantiates a new Server.
   *
   * @param useAI if true uses AI for text generation
   */
  public Server(boolean useAI) {
    this.useAI = useAI;
    if (useAI) {
      AITextGenerator.getInstance();
    }
  }

  private ServerStage currentStage;

  /**
   * Getter for useAI.
   *
   * @return true if AI should be used
   */
  public boolean isUseAI() {
    return useAI;
  }

  private boolean useAI;

  /**
   * Load stage.
   *
   * @param stage the stage
   */
  public void loadStage(ServerStage stage) {
    if (currentStage != null) {
      currentStage.stop();
    }
    currentStage = stage;
    if (currentStage != null) {
      currentStage.start(this);
    }
  }

  private final Logger logger = Logger.getLogger(Server.class.getName());

  /** The Sender. */
  final ExecutorService sender = Executors.newCachedThreadPool();

  /** The Worker. */
  final ExecutorService worker = Executors.newCachedThreadPool();

  /**
   * Execute a runnable on the sender thread pool.
   *
   * @param r the runnable to execute
   */
  public void sender(Runnable r) {
    sender.execute(r);
  }

  /**
   * Execute a runnable on the worker thread pool.
   *
   * @param r the runnable to execute
   */
  public void worker(Runnable r) {
    worker.execute(r);
  }

  private final Map<Integer, EndpointManager> endpointManagers = new ConcurrentHashMap<>();
  private final Map<Integer, MessageEndpoint> messageEndpoints = new ConcurrentHashMap<>();
  private final Map<Integer, SynchronisationEndpoint> synchronizationEndpoints =
      new ConcurrentHashMap<>();
  private final Map<Integer, MessageHandle<PlayerJoinEvent>> joinHandles =
      new ConcurrentHashMap<>();
  private final Map<Integer, MessageHandle<PlayerLeaveEvent>> leaveHandles =
      new ConcurrentHashMap<>();
  private ServerSocket serverSocket;

  /**
   * Socket accept socket.
   *
   * @return the socket
   * @throws IOException the io exception
   */
  public Socket socketAccept() throws IOException {
    return serverSocket.accept();
  }

  /**
   * Add client.
   *
   * @param id the id
   * @param manager the manager
   */
  public void addClient(int id, EndpointManager manager) {
    endpointManagers.put(id, manager);
    MessageEndpoint endpoint = new MessageEndpoint();
    manager.registerEndpoint(endpoint);

    messageEndpoints.put(id, endpoint);
    MessageHandle<PlayerJoinEvent> joinHandle =
        new MessageHandle<>(PlayerJoinEvent.class, () -> new PlayerJoinEvent(-1), null);
    endpoint.registerHandle(joinHandle);
    MessageHandle<PlayerLeaveEvent> leaveHandle =
        new MessageHandle<>(PlayerLeaveEvent.class, () -> new PlayerLeaveEvent(-1), null);
    endpoint.registerHandle(leaveHandle);
    joinHandles.put(id, joinHandle);
    leaveHandles.put(id, leaveHandle);
    PlayerJoinEvent joinEvent = new PlayerJoinEvent(id);

    SynchronisationEndpoint synchronisationEndpoint = new SynchronisationEndpoint();
    manager.registerEndpoint(synchronisationEndpoint);
    synchronizationEndpoints.put(id, synchronisationEndpoint);
    sender.execute(
        () -> {
          joinHandles.forEach((k, v) -> v.send(joinEvent));
          logger.log(Level.FINE, "Sending join events.");
        });
    logger.log(Level.INFO, "Client " + id + " joined.");
  }

  /**
   * Remove client.
   *
   * @param id the id
   */
  public void removeClient(int id) {
    if (!endpointManagers.containsKey(id)) {
      // logger.log(Level.SEVERE, "Client " + id + " not found.");
      return;
    } else {
      endpointManagers.remove(id).destroy();
      joinHandles.remove(id);
      leaveHandles.remove(id);
      messageEndpoints.remove(id);
      UserStorage.getInstance().removeUser(id);
      PlayerLeaveEvent leaveEvent = new PlayerLeaveEvent(id);
      sender.execute(
          () -> {
            leaveHandles.forEach((k, v) -> v.send(leaveEvent));
          });
      logger.log(Level.INFO, "Client " + id + " left.");
      worker.execute(
          () -> {
            if (endpointManagers.isEmpty() && currentStage instanceof GameStage) {
              logger.log(Level.WARNING, "Destroying game because all players left.");
              loadStage(new ConnectionStage());
            }
          });
    }
  }

  /**
   * Gets endpoint managers.
   *
   * @return the endpoint managers
   */
  public Map<Integer, EndpointManager> getEndpointManagers() {
    return Collections.unmodifiableMap(endpointManagers);
  }

  /**
   * Gets message endpoint.
   *
   * @param id the id
   * @return the message endpoint
   */
  public MessageEndpoint getMessageEndpoint(int id) {
    return messageEndpoints.get(id);
  }

  /**
   * Gets synchronisation endpoint for a given id.
   *
   * @param id the id
   * @return the synchronisation endpoint
   */
  public SynchronisationEndpoint getSynchronisationEndpoint(int id) {
    return synchronizationEndpoints.get(id);
  }

  /**
   * Connect.
   *
   * @param port the port
   * @throws IOException the io exception
   */
  public void connect(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    loadStage(new ConnectionStage());
  }

  /** Disconnects the clients and stops the server. */
  public void disconnect() {
    try {
      ConnectionHandler.createFor(null, null, null).destroy();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    currentStage.stop();
    endpointManagers.forEach(
        (k, v) -> {
          v.destroy();
        });
    try {
      serverSocket.close();
    } catch (IOException ignored) {
    }
    worker.shutdown();
    sender.shutdown();
    try {
      if (worker.awaitTermination(10, TimeUnit.SECONDS)) worker.shutdownNow();
      if (sender.awaitTermination(10, TimeUnit.SECONDS)) sender.shutdownNow();
    } catch (InterruptedException ignored) {
      // shut down
    }
  }
}
