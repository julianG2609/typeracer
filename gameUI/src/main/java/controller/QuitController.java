package controller;

import javafx.application.Platform;
import util.SceneLoader;
import util.Scenes;

/** Implements the Controller for the Quit Game Window. */
public class QuitController {

  Scenes last;

  /** Constructs a QuitGameController. */
  public QuitController() {}

  /**
   * Sets the last scene.
   *
   * @param last the last scene
   */
  public void setLast(Scenes last) {
    this.last = last;
  }

  /** Handles the Quit button. */
  public void onConfirm() {
    Platform.exit();
  }

  /** Handles the Cancel button. */
  public void onCancel() {
    if (last != null) {
      SceneLoader.getInstance().loadScene(last);
    }
  }
}
