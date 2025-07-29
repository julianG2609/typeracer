package controller;

import model.StartGameModel;
import store.ConnectionStore;
import store.GameStorage;
import store.UserStorage;
import util.SceneLoader;
import util.Scenes;

/** Implements the Controller for the Start Game Window. */
public class StartGameController {

  /** The Model. */
  public StartGameModel model;

  /**
   * Constructs a StartGameController.
   *
   * @param model the main menu model
   */
  public StartGameController(StartGameModel model) {
    this.model = model;
    GameStorage.getInstance()
        .addObserver(
            () -> {
              if (GameStorage.getInstance().getGame() != null) {
                SceneLoader.getInstance().loadScene(Scenes.GameWindow);
              }
            });
  }

  /** Handles the Start Game button. */
  public void onReady() {
    model.flip();
    // GameStorage.getInstance().setGame(new Game());
    ConnectionStore.getInstance().getClient().setReady(model.isReady());
  }

  /** Handles the Quit button. */
  public void onQuit() {
    SceneLoader.getInstance().loadScene(Scenes.QuitWindow, Scenes.StartGameWindow);
  }

  /** Handles the Settings button. */
  public void onSettings() {
    SceneLoader.getInstance().loadScene(Scenes.SettingsWindow);
  }

  /**
   * On set username.
   *
   * @param username the username
   */
  public void onSetUsername(String username) {
    if (UserStorage.getInstance().getName() != null
        && UserStorage.getInstance().getName().equals(username)) {
      return;
    }
    UserStorage.getInstance().setName(username);
    model.setUsernametaken(false);
    if (username.trim().isEmpty()) {
      model.setUsernameEmpty(true);
    } else {
      model.setUsernameEmpty(false);
      ConnectionStore.getInstance().getClient().setUsername(username);
    }
  }

  /**
   * On colour select.
   *
   * @param colour the selected car colour
   */
  public void onColourSelect(String colour) {
    if (colour == null) {
      colour = "blue";
    }
    colour = colour.toLowerCase();
    if (UserStorage.getInstance().getColour() != null
        && UserStorage.getInstance().getColour().equals(colour)) {
      return;
    }
    UserStorage.getInstance().setColour(colour);
    ConnectionStore.getInstance().getClient().setCarColour(colour);
  }
}
