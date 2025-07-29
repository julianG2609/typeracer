package util;

/** Enum to simplify access to scenes. */
public enum Scenes {
  /** Connect Window. */
  ConnectWindow("ConnectWindow.fxml"),
  /** Game Window. */
  GameWindow("GameWindow.fxml"),
  /** GameOver Window. */
  GameOverWindow("GameOverWindow.fxml"),
  /** Quit Window. */
  QuitWindow("QuitGameWindow.fxml"),
  /** Settings Window. */
  SettingsWindow("SettingsWindow.fxml"),
  /** StartGame Window */
  StartGameWindow("StartGameWindow.fxml"),
  ;

  /** the fxml file of the scene. */
  public final String file;

  Scenes(String file) {
    this.file = file;
  }
}
