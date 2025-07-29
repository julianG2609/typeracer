package controller;

import store.RankingStorage;
import util.SceneLoader;
import util.Scenes;

/** Implements the Controller for the Game Over screen. */
public class GameOverController {

  /** Constructs a GameOverController. */
  public GameOverController() {}

  /** Handles the Menu button. */
  public void onMenu() {
    RankingStorage.getInstance().resetRankings();
    SceneLoader.getInstance().loadScene(Scenes.StartGameWindow);
  }

  /** Handles the Quit button. */
  public void onQuit() {
    SceneLoader.getInstance().loadScene(Scenes.QuitWindow, Scenes.GameOverWindow);
  }
}
