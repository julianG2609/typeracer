package gamelogic;

import data.PlayerWPMs;
import events.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.StartGameModel;
import store.ConnectionStore;
import store.GameStorage;
import store.RankingStorage;
import store.UserStorage;

/** The type Event handler. */
public class EventHandler implements EventHandlerInterface {

  /** Instantiates a new Event handler. */
  public EventHandler() {}

  /**
   * Handle player join event.
   *
   * @param event the event
   */
  @Override
  public void handlePlayerJoinEvent(PlayerJoinEvent event) {
    //    TODO.
    //    GameStorage.getInstance().getGame().addPlayer(event.getPlayer());
  }

  /**
   * Handle start game event.
   *
   * @param event the event
   */
  @Override
  public void handleStartGameEvent(StartGameEvent event) {
    GameStorage.getInstance().getGame().startGame();
  }

  /**
   * Handle player leave event.
   *
   * @param event the event
   */
  @Override
  public void handlePlayerLeaveEvent(PlayerLeaveEvent event) {
    //    TODO.
    //    GameStorage.getInstance().getGame().removePlayer(event.getPlayer());
  }

  /**
   * Handle set car colour event.
   *
   * @param event the event
   */
  @Override
  public void handleSetCarColourEvent(SetCarColourEvent event) {
    UserStorage.getInstance().setName(event.getCarColour());
  }

  /**
   * Handle car colour taken event.
   *
   * @param event the event
   */
  @Override
  public void handleCarColourTakenEvent(CarColourTakenEvent event) {
    StartGameModel.getInstance().setCarColourTaken(!event.isSuccess());
  }

  /**
   * Handle set username event.
   *
   * @param event the event
   */
  @Override
  public void handleSetUsernameEvent(SetUsernameEvent event) {
    UserStorage.getInstance().setName(event.getUsername());
  }

  /**
   * Handle username taken event.
   *
   * @param event the event
   */
  @Override
  public void handleUsernameTakenEvent(UsernameTakenEvent event) {
    StartGameModel.getInstance().setUsernametaken(!event.isSuccess());
  }

  /**
   * Handle stop game event.
   *
   * @param event the event
   */
  @Override
  public void handleStopGameEvent(StopGameEvent event) {}

  /**
   * Handle player finish event.
   *
   * @param event the event
   */
  @Override
  public void handlePlayerFinishEvent(PlayerFinishEvent event) {
    GameStorage.getInstance().setGame(null);
  }

  /**
   * Handle create game event.
   *
   * @param event the event
   */
  @Override
  public void handleCreateGameEvent(CreateGameEvent event) {
    LocalPlayer localPlayer =
        new LocalPlayer(
            ConnectionStore.getInstance().getClient().getId(),
            UserStorage.getInstance().getName(),
            UserStorage.getInstance().getColour());
    Game game = GameFactory.createGame();
    game.addPlayer(localPlayer);
    GameStorage.getInstance().setGame(game);
    ConnectionStore.getInstance().getClient().setLocalPlayer(localPlayer);
  }

  @Override
  public void handlePlayerScoreEvent(PlayerScoreEvent event) {
    List<PlayerWPMs> wpmRanking = new ArrayList<>();
    for (Player player : GameStorage.getInstance().getGame().getPlayers()) {
      System.out.println(player.getWPM());
      wpmRanking.add(new PlayerWPMs(player.getWPM(), player.getName()));
    }
    RankingStorage.getInstance()
        .setWpmRanking(
            wpmRanking.stream().sorted(Comparator.comparingInt(PlayerWPMs::wpm)).toList());
    System.out.println(wpmRanking);
    GameStorage.getInstance().setGame(null);
    System.out.println(event.getOrderedScores());
    RankingStorage.getInstance().setOverallRanking(event.getOrderedScores());
  }

  @Override
  public void handleTextEvent(TextEvent event) {
    GameStorage.getInstance().getGame().setText(event.getText());
  }

  @Override
  public void handleRemotePlayer(Object player) {
    GameStorage.getInstance().getGame().addPlayer((RemotePlayer) player);
  }
}
