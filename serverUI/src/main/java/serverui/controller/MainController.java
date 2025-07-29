package serverui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import serverui.elemnts.LogElement;
import serverui.model.MainModel;
import serverui.util.SceneLoader;
import serverui.util.Scenes;

/** Implements the MainController */
public class MainController implements Initializable {

  @FXML private TextField port;
  @FXML private Button start;
  @FXML private VBox list;
  @FXML private ScrollPane scrollPane;
  @FXML private CheckBox useAI;

  private boolean bound = true;

  /** Constructs a MainController. */
  public MainController() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    MainModel.getInstance()
        .addObserver(
            (model) -> {
              if (model.isRunning()) {
                useAI.setDisable(true);
                start.setText("Stop");
                port.setDisable(true);
              } else {
                useAI.setDisable(false);
                start.setText("Start");
                port.setDisable(false);
              }
            });
    port.textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                onSetPort(-1);
              }
              onSetPort(Integer.parseInt(newValue));
            });
    if (port.getText().matches("\\d*")) {
      onSetPort(Integer.parseInt(port.getText()));
    }

    MainModel.getInstance()
        .addLogHandler(
            (log) -> {
              Platform.runLater(
                  () -> {
                    list.getChildren().add(new LogElement(log));
                    // System.out.println(list.getChildren());
                  });
            });
    scrollPane.setPannable(false);
    scrollPane.vvalueProperty().bind(list.heightProperty());
    scrollPane.setOnMouseClicked(
        (event) -> {
          bound = !bound;
          if (bound) {
            scrollPane.vvalueProperty().bind(list.heightProperty());
          } else {
            scrollPane.vvalueProperty().unbind();
          }
        });
  }

  /** Handles the Quit button. */
  public void onQuitButton() {
    SceneLoader.getInstance().loadScene(Scenes.QuitWindow, Scenes.MainWindow);
  }

  /** Handles the Set button. */
  public void onSetButton() {
    MainModel.getInstance().setUseAI(useAI.isSelected());
    MainModel.getInstance().flipRunning();
  }

  /**
   * Sets the port.
   *
   * @param port the port
   */
  public void onSetPort(int port) {
    MainModel.getInstance().setPort(port);
  }
}
