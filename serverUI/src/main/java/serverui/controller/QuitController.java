package serverui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import serverui.model.MainModel;
import serverui.util.SceneLoader;
import serverui.util.Scenes;

/** Implements the Controller for the Quit Window. */
public class QuitController implements Initializable {
  @FXML VBox parent;

  Scenes last;

  /** Constructs a Quit Controller. */
  public QuitController() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    parent
        .getProperties()
        .addListener(
            (MapChangeListener<? super Object, ? super Object>)
                (newVal) -> {
                  if (newVal.getKey() == "status") {
                    Scenes status = (Scenes) newVal.getMap().get("status");
                    setLast(status);
                  }
                });
  }

  /**
   * Sets the last scene.
   *
   * @param last the last scene
   */
  public void setLast(Scenes last) {
    this.last = last;
  }

  /** Handles the Quit button. */
  public void onConfirmButton() {
    MainModel.getInstance().onQuit();
    Platform.exit();
  }

  /** Handles the Cancel button. */
  public void onCancelButton() {
    if (last != null) {
      SceneLoader.getInstance().loadScene(last);
    }
  }
}
