package view;

import controller.SettingsController;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import store.SettingsStorage;

/** Implements the View of the Settings window. */
public class SettingsView implements Initializable {

  @FXML CheckBox darkmode;
  @FXML javafx.scene.Parent parent;

  SettingsController controller;

  /** Constructors a SettingsView. */
  public SettingsView() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    controller = new SettingsController();

    if (SettingsStorage.getInstance().isDarkmode()) {
      parent.getStylesheets().add("DarkMode.css");
      darkmode.setSelected(true);
    }
  }

  /**
   * Handles the Back button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onBackButton(ActionEvent actionEvent) {
    controller.onBack();
  }

  /**
   * Handles the user interacting with the Dark Mode checkbox.
   *
   * @param actionEvent the action event
   */
  public void onDarkModeSelect(ActionEvent actionEvent) {
    controller.onDarkMode(darkmode.isSelected());
  }

  /**
   * Handles the Confirm button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onConfirmButton(ActionEvent actionEvent) {
    controller.onConfirm();
  }
}
