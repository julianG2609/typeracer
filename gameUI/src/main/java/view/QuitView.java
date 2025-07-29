package view;

import controller.QuitController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import store.SettingsStorage;
import util.Scenes;

/** Implements the View of the Quit Game window. */
public class QuitView implements Initializable {

  @FXML VBox parent;

  /** Constructs a QuitView. */
  public QuitView() {}

  QuitController controller;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    controller = new QuitController();

    if (SettingsStorage.getInstance().isDarkmode()) {
      parent.getStylesheets().add("DarkMode.css");
    }

    parent
        .getProperties()
        .addListener(
            (MapChangeListener<? super Object, ? super Object>)
                (newVal) -> {
                  if (newVal.getKey() == "status") {
                    Scenes status = (Scenes) newVal.getMap().get("status");
                    controller.setLast(status);
                  }
                });
  }

  /**
   * Handles the Confirm button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onConfirmButton(ActionEvent actionEvent) {
    controller.onConfirm();
  }

  /**
   * Handles the Cancel button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onCancelButton(ActionEvent actionEvent) {
    controller.onCancel();
  }
}
