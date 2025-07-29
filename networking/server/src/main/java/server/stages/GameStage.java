package server.stages;

import data.PlayerScore;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import endpoints.messaging.MessageEndpoint;
import endpoints.messaging.MessageHandle;
import endpoints.synchronisation.SynchronisationEndpoint;
import events.PlayerFinishEvent;
import events.PlayerScoreEvent;
import events.StartGameEvent;
import events.TextEvent;
import gamelogic.LocalPlayer;
import gamelogic.Player;
import gamelogic.RemotePlayer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;
import server.storage.UserStorage;
import server.util.Text;
import textgeneration.AITextGenerator;

/** The GameStage class represents the game stage of the server. */
public class GameStage extends ServerStage {

  /** Constructs a new GameStage. */
  public GameStage() {}

  private final HashMap<Integer, Players> players = new HashMap<>();
  private Server server;

  private final HashMap<Integer, Handles> handles = new HashMap<>();

  private final Map<Integer, PlayerScore> finishedPlayers = new ConcurrentHashMap<>();

  private final Logger logger = Logger.getLogger(Server.class.getName());

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  @Override
  public void start(Server server) {
    this.server = server;
    server.worker(
        () -> {
          String text = "Text not generated!!!";
          if (server.isUseAI()) {
            try {
              text = AITextGenerator.getInstance().getText();
            } catch (InterruptedException e) {
              server.disconnect();
            }
          } else {
            text = Text.getRandomText().getContent();
          }

          TextEvent textEvent = new TextEvent(text);
          for (int id : server.getEndpointManagers().keySet()) {

            MessageHandle<StartGameEvent> startHandle =
                new MessageHandle<>(StartGameEvent.class, StartGameEvent::new, (event) -> {});
            server.getMessageEndpoint(id).registerHandle(startHandle);
            MessageHandle<PlayerFinishEvent> finishHandle =
                new MessageHandle<>(
                    PlayerFinishEvent.class, () -> new PlayerFinishEvent(-1), this::handleFinish);
            server.getMessageEndpoint(id).registerHandle(finishHandle);
            MessageHandle<PlayerScoreEvent> scoreHandle =
                new MessageHandle<>(PlayerScoreEvent.class, PlayerScoreEvent::new, (e) -> {});
            server.getMessageEndpoint(id).registerHandle(scoreHandle);

            MessageHandle<TextEvent> textHandle =
                new MessageHandle<>(TextEvent.class, TextEvent::new, (e) -> {});
            server.getMessageEndpoint(id).registerHandle(textHandle);

            handles.put(id, new Handles(startHandle, finishHandle, scoreHandle, textHandle));

            textHandle.send(textEvent);

            SynchronisationEndpoint endpoint = server.getSynchronisationEndpoint(id);
            LocalPlayer localPlayer = getLocalPlayer(server, id);
            List<RemotePlayer> remotePlayers = new ArrayList<>();
            for (int iid : server.getEndpointManagers().keySet()) {
              if (iid != id) {
                RemotePlayer remotePlayer =
                    new RemotePlayer(id, localPlayer.getName(), localPlayer.getColour());
                remotePlayers.add(remotePlayer);
                remotePlayer.addObserver(
                    () -> {
                      server.sender(
                          () -> {
                            server.getSynchronisationEndpoint(iid).synchronize(remotePlayer);
                          });
                    });
                server.getSynchronisationEndpoint(iid).register(remotePlayer);
              }
            }
            endpoint.register((Player) localPlayer);
            Players playersTmp = new Players(localPlayer, remotePlayers);
            players.put(id, playersTmp);
          }
          server.sender(
              () -> {
                StartGameEvent event = new StartGameEvent();
                for (int id : server.getEndpointManagers().keySet()) {
                  handles.get(id).startHandle.send(event);
                }
              });
        });
  }

  private LocalPlayer getLocalPlayer(Server server, int id) {
    LocalPlayer localPlayer =
        new LocalPlayer(
            id,
            UserStorage.getInstance().getUser(id).getUsername(),
            UserStorage.getInstance().getUser(id).getColour());
    localPlayer.addObserver(
        () -> {
          server.sender(
              () -> {
                logger.log(Level.FINE, "Updating player: " + id);
                while (players.get(id) == null) {
                  Thread.onSpinWait();
                }
                List<RemotePlayer> remotePlayers = players.get(id).remotePlayers;
                for (RemotePlayer remotePlayer : remotePlayers) {
                  localPlayer.copy(remotePlayer);
                }
              });
        });
    return localPlayer;
  }

  private synchronized void handleFinish(PlayerFinishEvent event) {
    if (finishedPlayers.containsKey(event.id)) return;
    finishedPlayers.put(
        event.id,
        new PlayerScore(
            System.currentTimeMillis(), UserStorage.getInstance().getUser(event.id).getUsername()));
    logger.log(Level.FINE, "Finished player " + event.getId());
    if (finishedPlayers.size() == players.size()) {
      logger.log(Level.INFO, "Round over");
      server.sender(
          () -> {
            PlayerScoreEvent scoreEvent =
                new PlayerScoreEvent(finishedPlayers.values().stream().toList());
            for (Handles handle : handles.values()) {
              handle.scoreHandle.send(scoreEvent);
            }
            server.loadStage(new ConnectionStage());
          });
    }
  }

  @Override
  public void stop() {
    server
        .getEndpointManagers()
        .forEach(
            (id, endpoint) -> {
              server.getSynchronisationEndpoint(id).clear();
              MessageEndpoint messageEndpoint = server.getMessageEndpoint(id);
              messageEndpoint.unregisterHandle(handles.get(id).finishHandle);
              messageEndpoint.unregisterHandle(handles.get(id).startHandle);
              messageEndpoint.unregisterHandle(handles.get(id).finishHandle);
              messageEndpoint.unregisterHandle(handles.get(id).textHandle);
            });
    logger.log(Level.FINE, "Game stage destructed.");
  }

  private record Players(LocalPlayer thisPlayer, List<RemotePlayer> remotePlayers) {}

  private record Handles(
      MessageHandle<StartGameEvent> startHandle,
      MessageHandle<PlayerFinishEvent> finishHandle,
      MessageHandle<PlayerScoreEvent> scoreHandle,
      MessageHandle<TextEvent> textHandle) {}
}
