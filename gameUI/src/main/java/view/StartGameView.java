package view;

import controller.StartGameController;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.StartGameModel;
import store.SettingsStorage;

/** Implements the View of the Start Game window. */
public class StartGameView implements Initializable {

  @FXML ComboBox<String> color;
  @FXML TextField username;
  @FXML ToggleButton readyButton;
  @FXML Text usernameErrorText;
  @FXML Text carColourTaken;
  @FXML ImageView logo;
  @FXML javafx.scene.Parent parent;
  @FXML HBox settings;

  private StartGameModel startGameModel = StartGameModel.resetInstance();

  ObservableList<String> colours =
      FXCollections.observableArrayList("Blue", "Pink", "Green", "Black", "Red", "Yellow");

  StartGameController controller;

  /** Constructs the StartGameView */
  public StartGameView() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    controller = new StartGameController(startGameModel);

    if (SettingsStorage.getInstance().isDarkmode()) {
      parent.getStylesheets().add("DarkMode.css");
      String imgPath = logo.getImage().getUrl().replace("logo_black.png", "logo_dark_mode.png");
      logo.setImage(new javafx.scene.image.Image(imgPath));
    }

    logo.fitWidthProperty().bind(settings.widthProperty());

    color.setItems(colours);

    usernameErrorText.setVisible(false);
    carColourTaken.setVisible(false);
    initInput();

    controller.onSetUsername(username.getText());
    controller.onColourSelect(color.getValue());

    updateUiElements();
    startGameModel.addObserver(
        () -> {
          updateUiElements();
        });
  }

  private void updateUiElements() {
    readyButton.setDisable(startGameModel.isUsernametaken() || startGameModel.isCarColourTaken());
    String errorMessage = "";
    if (startGameModel.isUsernametaken()) {
      errorMessage = "This name is already taken!";
    }
    if (startGameModel.isUsernameEmpty()) {
      errorMessage = "Username cannot be empty!";
    }
    usernameErrorText.setText(errorMessage);
    usernameErrorText.setVisible(!errorMessage.isEmpty());
    carColourTaken.setVisible(startGameModel.isCarColourTaken());
  }

  private void initInput() {
    username.setOnKeyPressed(this::handleKeyPressed);
    usernameErrorText.setOnKeyPressed(this::handleKeyPressed);
  }

  private void handleKeyPressed(KeyEvent event) {
    if (!Objects.equals(event.getText(), "")) {
      Platform.runLater(
          () -> {
            controller.onSetUsername(username.getText());
          });
    }
    event.consume();
  }

  /**
   * Handles the colour being selected.
   *
   * @param actionEvent the action event
   */
  public void colourSelected(ActionEvent actionEvent) {
    controller.onColourSelect(color.getValue());
  }

  /**
   * Handles the Quit button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onQuitButton(ActionEvent actionEvent) {
    controller.onQuit();
  }

  /**
   * Handles the Ready button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onReadyButton(ActionEvent actionEvent) {
    controller.onReady();
    if (readyButton.isSelected()) {
      readyButton.setText("Ready!");
    } else {
      readyButton.setText("Ready up");
    }
  }

  /**
   * Handles the Settings button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onSettingsButton(ActionEvent actionEvent) {
    controller.onSettings();
  }

  /**
   * Handles a username being entered.
   *
   * @param actionEvent the action event
   */
  public void onUsername(ActionEvent actionEvent) {
    controller.onSetUsername(username.getText());
  }
}
