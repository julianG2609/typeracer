package networking;

import conversion.json.JSONConverter;
import endpoints.EndpointManager;
import endpoints.messaging.MessageEndpoint;
import endpoints.messaging.MessageHandle;
import endpoints.synchronisation.SynchronisationEndpoint;
import events.*;
import gamelogic.LocalPlayer;
import gamelogic.Player;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import transport.exceptions.TransportException;
import transport.exceptions.TransportIOException;
import util.DaemonThreadFactory;

/** The type Client. */
public class Client {
  private EventHandlerInterface eventHandler;

  /**
   * Instantiates a new Client.
   *
   * @param eventHandler the event handler
   */
  public Client(EventHandlerInterface eventHandler) {
    this.eventHandler = eventHandler;
  }

  private Executor worker = Executors.newFixedThreadPool(2, new DaemonThreadFactory());

  private EndpointManager endpointManager;
  private MessageEndpoint messageEndpoint;
  private SynchronisationEndpoint synchronisationEndpoint;

  private MessageHandle<PlayerJoinEvent> joinHandle;
  private MessageHandle<PlayerLeaveEvent> leaveHandle;
  private MessageHandle<SetUsernameEvent> usernameHandle;
  private MessageHandle<SetCarColourEvent> colourHandle;
  private MessageHandle<ReadyEvent> readyHandle;
  private MessageHandle<PlayerFinishEvent> finishHandle;

  /**
   * Gets id.
   *
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  private volatile Integer id = null;

  /**
   * Connect.
   *
   * @param host the host
   * @param port the port
   * @param onAbort the on abort
   * @param onConnect the on connect
   */
  public void connect(String host, int port, Runnable onAbort, Runnable onConnect) {
    worker.execute(
        () -> {
          endpointManager = new EndpointManager(new JSONConverter(), host, port);
          try {
            endpointManager.connect();
          } catch (TransportException e) {
            onAbort.run();
            return;
          }

          messageEndpoint = new MessageEndpoint();
          endpointManager.registerEndpoint(messageEndpoint);

          synchronisationEndpoint = new SynchronisationEndpoint();
          endpointManager.registerEndpoint(synchronisationEndpoint);

          joinHandle =
              new MessageHandle<>(
                  PlayerJoinEvent.class, () -> new PlayerJoinEvent(-1), this::onPlayerJoin);
          messageEndpoint.registerHandle(joinHandle);
          leaveHandle =
              new MessageHandle<>(
                  PlayerLeaveEvent.class, () -> new PlayerLeaveEvent(-1), this::onPlayerLeave);
          messageEndpoint.registerHandle(leaveHandle);

          MessageHandle<CarColourTakenEvent> carColourTakenHandle =
              new MessageHandle<>(
                  CarColourTakenEvent.class,
                  () -> new CarColourTakenEvent(-1, false),
                  (event) -> {
                    eventHandler.handleCarColourTakenEvent(event);
                  });
          messageEndpoint.registerHandle(carColourTakenHandle);

          MessageHandle<UsernameTakenEvent> takenHandle =
              new MessageHandle<>(
                  UsernameTakenEvent.class,
                  () -> new UsernameTakenEvent(-1, false),
                  (event) -> {
                    eventHandler.handleUsernameTakenEvent(event);
                  });
          messageEndpoint.registerHandle(takenHandle);
          usernameHandle =
              new MessageHandle<>(
                  SetUsernameEvent.class, () -> new SetUsernameEvent(-1, null), (event) -> {});
          messageEndpoint.registerHandle(usernameHandle);

          colourHandle =
              new MessageHandle<>(
                  SetCarColourEvent.class, () -> new SetCarColourEvent(-1, null), (event) -> {});
          messageEndpoint.registerHandle(colourHandle);

          readyHandle =
              new MessageHandle<>(ReadyEvent.class, () -> new ReadyEvent(false, -1), (event) -> {});
          messageEndpoint.registerHandle(readyHandle);

          MessageHandle<CreateGameEvent> createGameHandle =
              new MessageHandle<>(
                  CreateGameEvent.class, CreateGameEvent::new, eventHandler::handleCreateGameEvent);
          messageEndpoint.registerHandle(createGameHandle);

          MessageHandle<StartGameEvent> startHandle =
              new MessageHandle<>(
                  StartGameEvent.class, StartGameEvent::new, eventHandler::handleStartGameEvent);
          messageEndpoint.registerHandle(startHandle);

          MessageHandle<TextEvent> textHandle =
              new MessageHandle<>(TextEvent.class, TextEvent::new, eventHandler::handleTextEvent);
          messageEndpoint.registerHandle(textHandle);

          finishHandle =
              new MessageHandle<>(
                  PlayerFinishEvent.class,
                  () -> new PlayerFinishEvent(-1),
                  eventHandler::handlePlayerFinishEvent);
          messageEndpoint.registerHandle(finishHandle);

          MessageHandle<PlayerScoreEvent> scoreHandle =
              new MessageHandle<>(
                  PlayerScoreEvent.class,
                  PlayerScoreEvent::new,
                  eventHandler::handlePlayerScoreEvent);
          messageEndpoint.registerHandle(scoreHandle);

          try {
            synchronisationEndpoint.register(new RemotePlayerReceiver(eventHandler));
          } catch (TransportIOException e) {
            throw new RuntimeException(e);
          }

          while (id == null) {
            Thread.onSpinWait();
          }

          onConnect.run();
        });
  }

  private void onPlayerJoin(PlayerJoinEvent event) {
    if (id == null) {
      id = event.getId();
    }
    eventHandler.handlePlayerJoinEvent(event);
  }

  private void onPlayerLeave(PlayerLeaveEvent event) {
    eventHandler.handlePlayerLeaveEvent(event);
  }

  /**
   * Sets username.
   *
   * @param username the username
   */
  public void setUsername(String username) {
    usernameHandle.send(new SetUsernameEvent(id, username));
  }

  /**
   * Sets car colour.
   *
   * @param colour the colour
   */
  public void setCarColour(String colour) {
    colourHandle.send(new SetCarColourEvent(id, colour));
  }

  /**
   * Sets ready.
   *
   * @param ready the ready
   */
  public void setReady(boolean ready) {
    readyHandle.send(new ReadyEvent(ready, id));
  }

  /**
   * Sets local player.
   *
   * @param localPlayer the local player
   */
  public void setLocalPlayer(LocalPlayer localPlayer) {
    synchronisationEndpoint.register((Player) localPlayer);
    localPlayer.addObserver(
        () -> {
          worker.execute(
              () -> {
                synchronisationEndpoint.synchronize(localPlayer);
              });
        });
  }

  /** Finish. */
  public void finish() {
    finishHandle.send(new PlayerFinishEvent(id));
  }
}
