package serverui.util;

/** Enum to simplify access to scenes. */
public enum Scenes {
  /** Main Window. */
  MainWindow("MainWindow.fxml"),
  /** Quit Window. */
  QuitWindow("QuitWindow.fxml"),
  ;

  /** the fxml file of the scene. */
  public final String file;

  Scenes(String file) {
    this.file = file;
  }
}
