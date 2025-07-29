package controller;

import controller.util.DaemonThreadFactory;
import gamelogic.Game;
import gamelogic.LocalPlayer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import store.ConnectionStore;
import store.GameStorage;
import store.RankingStorage;
import util.SceneLoader;
import util.Scenes;

/** Implements the Controller for the Game Window. */
public class GameController {

  private final Executor executor = Executors.newCachedThreadPool(new DaemonThreadFactory());

  /** The Game. */
  public Game game;

  private boolean processInput = false;
  private LocalPlayer currentPlayer;

  /**
   * Returns the current game.
   *
   * @return the active game
   */
  public Game getGame() {
    return game;
  }

  /** Constructs a GameController. */
  public GameController() {
    // GameStorage.getInstance().setGame(game);
    game = GameStorage.getInstance().getGame();
    currentPlayer =
        (LocalPlayer)
            game.getPlayers().stream().filter((it) -> it instanceof LocalPlayer).findFirst().get();
    RankingStorage.getInstance()
        .addObserver(
            () -> {
              if (RankingStorage.getInstance().getOverallRanking() != null) {
                SceneLoader.getInstance().loadScene(Scenes.GameOverWindow);
              }
            });
  }

  /** Handles the Quit button. */
  public void onQuit() {
    SceneLoader.getInstance().loadScene(Scenes.QuitWindow, Scenes.GameWindow);
  }

  /** Switches to the Game Over screen if the game is finished. */
  public void onGameOver() {
    ConnectionStore.getInstance().getClient().finish();
  }

  /** Starts a new game when the UI is loaded. */
  public void onStart() {
    game.startGame();
  }

  /**
   * Checks for input validity after every key press.
   *
   * @param character the pressed character
   */
  public void onType(String character) {
    executor.execute(
        () -> {
          if (processInput) {
            currentPlayer.onInput(character);
          }
        });
  }

  /**
   * Sets if keystrokes are processed.
   *
   * @param processInput if keystrokes are processed
   */
  public void setProcessInput(boolean processInput) {
    this.processInput = processInput;
    System.out.println("setProcessInput " + processInput);
  }
}
