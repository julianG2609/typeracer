package controller;

import store.SettingsStorage;
import util.SceneLoader;
import util.Scenes;

/** Implements the Controller for the Settings Window. */
public class SettingsController {

  private boolean confirm;

  /** Constructs a SettingsController. */
  public SettingsController() {}

  /** Handles the Back button. */
  public void onBack() {
    confirm = false;
    SceneLoader.getInstance().loadScene(Scenes.StartGameWindow);
  }

  /**
   * Sets the confirm value to true when the user checks dark mode, vice versa.
   *
   * @param selected the selected
   */
  public void onDarkMode(boolean selected) {
    confirm = selected;
  }

  /** Handles the Confirm button. */
  public void onConfirm() {
    if (confirm) {
      SettingsStorage.getInstance().setDarkmode(true);
      SceneLoader.getInstance().loadScene(Scenes.StartGameWindow);
    } else {
      SettingsStorage.getInstance().setDarkmode(false);
      SceneLoader.getInstance().loadScene(Scenes.StartGameWindow);
    }
  }
}
