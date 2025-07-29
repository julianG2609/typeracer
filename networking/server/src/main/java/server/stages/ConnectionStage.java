package server.stages;

import conversion.json.JSONConverter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import endpoints.EndpointManager;
import endpoints.messaging.MessageEndpoint;
import endpoints.messaging.MessageHandle;
import events.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ConnectionHandler;
import server.Server;
import server.storage.UserStorage;

/** The type Connection stage. */
public class ConnectionStage extends ServerStage {

  private Server server;
  private ConnectionHandler connectionHandler;

  private final HashMap<Integer, Handles> handles = new HashMap<>();
  private final HashMap<Integer, Boolean> ready = new HashMap<>();

  private final Logger logger = Logger.getLogger(Server.class.getName());

  /** Instantiates a new Connection stage. */
  public ConnectionStage() {}

  @Override
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public void start(Server server) {
    this.server = server;
    server.getEndpointManagers().forEach(this::setupClient);
    try {
      connectionHandler =
          ConnectionHandler.createFor(server, new JSONConverter(), this::handleConnection);
    } catch (IOException ignored) {
    }
    connectionHandler.setAllowConnections(true);
  }

  private void handleConnection(int id, EndpointManager client) {
    server.worker(
        () -> {
          client.setOnDestroy(() -> server.removeClient(id));
          server.addClient(id, client);
          setupClient(id, client);
        });
  }

  private void setupClient(int id, EndpointManager client) {
    MessageEndpoint messageEndpoint = server.getMessageEndpoint(id);
    MessageHandle<SetUsernameEvent> usernameHandle =
        new MessageHandle<>(
            SetUsernameEvent.class, () -> new SetUsernameEvent(-1, ""), this::setUsername);
    MessageHandle<SetCarColourEvent> colourHandle =
        new MessageHandle<>(
            SetCarColourEvent.class, () -> new SetCarColourEvent(-1, ""), this::setCarColour);
    MessageHandle<UsernameTakenEvent> takenHandle =
        new MessageHandle<>(
            UsernameTakenEvent.class, () -> new UsernameTakenEvent(-1, false), (event) -> {});
    MessageHandle<ReadyEvent> readyHandle =
        new MessageHandle<>(ReadyEvent.class, () -> new ReadyEvent(false, -1), this::setReady);
    MessageHandle<CarColourTakenEvent> carColourTakenHandle =
        new MessageHandle<>(
            CarColourTakenEvent.class, () -> new CarColourTakenEvent(-1, false), (event) -> {});

    messageEndpoint.registerHandle(takenHandle);
    messageEndpoint.registerHandle(usernameHandle);
    messageEndpoint.registerHandle(colourHandle);
    messageEndpoint.registerHandle(readyHandle);
    messageEndpoint.registerHandle(carColourTakenHandle);
    handles.put(
        id,
        new Handles(usernameHandle, colourHandle, takenHandle, readyHandle, carColourTakenHandle));
    ready.put(id, false);
  }

  private void returnClient(int id, EndpointManager client) {
    MessageEndpoint messageEndpoint = server.getMessageEndpoint(id);
    messageEndpoint.unregisterHandle(handles.get(id).usernameHandle);
    messageEndpoint.unregisterHandle(handles.get(id).carColourHandle);
    messageEndpoint.unregisterHandle(handles.get(id).takenHandle);
    messageEndpoint.unregisterHandle(handles.get(id).readyHandle);
    messageEndpoint.unregisterHandle(handles.get(id).carColourTakenHandle);
    ready.clear();
  }

  private void setUsername(SetUsernameEvent event) {
    if (UserStorage.getInstance().usernameExists(event.id, event.username)) {
      server.sender(
          () -> {
            handles.get(event.id).takenHandle.send(new UsernameTakenEvent(event.id, false));
          });
    } else {
      server.worker(
          () -> {
            UserStorage.getInstance().getUser(event.id).setUsername(event.username);
            logger.log(Level.FINE, "Username of " + event.id + " set to " + event.username);
          });
      server.sender(
          () -> {
            handles.get(event.id).takenHandle.send(new UsernameTakenEvent(event.id, true));
          });
    }
  }

  private void setCarColour(SetCarColourEvent event) {
    if (UserStorage.getInstance().carColourExists(event.id, event.colour)) {
      server.sender(
          () -> {
            handles
                .get(event.id)
                .carColourTakenHandle
                .send(new CarColourTakenEvent(event.id, false));
          });
    } else {
      server.worker(
          () -> {
            UserStorage.getInstance().getUser(event.id).setColour(event.colour);
            logger.log(Level.FINE, "CarColour of " + event.id + " set to " + event.getCarColour());
          });
      server.sender(
          () -> {
            handles
                .get(event.id)
                .carColourTakenHandle
                .send(new CarColourTakenEvent(event.id, true));
          });
    }
  }

  private void setReady(ReadyEvent event) {
    server.worker(
        () -> {
          ready.put(event.getId(), event.isReady());
          logger.log(Level.FINE, "Ready state of " + event.getId() + " set to " + event.isReady());
          if (ready.entrySet().stream()
              .allMatch(
                  (it) ->
                      it.getValue() || !server.getEndpointManagers().containsKey(it.getKey()))) {
            logger.log(Level.INFO, "Starting a new game.");
            server.sender(
                () -> {
                  for (int id : server.getEndpointManagers().keySet()) {
                    MessageHandle<CreateGameEvent> createGameHandle =
                        new MessageHandle<>(
                            CreateGameEvent.class, CreateGameEvent::new, (ignored) -> {});
                    MessageEndpoint messageEndpoint = server.getMessageEndpoint(id);
                    messageEndpoint.registerHandle(createGameHandle);
                    createGameHandle.send(new CreateGameEvent());
                    messageEndpoint.unregisterHandle(createGameHandle);
                  }
                });
            server.worker(() -> server.loadStage(new GameStage()));
          }
        });
  }

  @Override
  public void stop() {
    connectionHandler.setAllowConnections(false);
    server.getEndpointManagers().forEach(this::returnClient);
  }

  private record Handles(
      MessageHandle<SetUsernameEvent> usernameHandle,
      MessageHandle<SetCarColourEvent> carColourHandle,
      MessageHandle<UsernameTakenEvent> takenHandle,
      MessageHandle<ReadyEvent> readyHandle,
      MessageHandle<CarColourTakenEvent> carColourTakenHandle) {}
}
