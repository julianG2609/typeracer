package view;

import controller.ConnectController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.ConnectModel;

/** Implements the View of the Connect window. */
public class ConnectView implements Initializable {

  @FXML TextField ip;

  @FXML TextField port;

  @FXML Text error;

  @FXML Button connect;

  /** Constructs a ConnectView. */
  public ConnectView() {}

  ConnectController controller;
  ConnectModel connectModel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    connectModel = new ConnectModel();
    controller = new ConnectController(connectModel);

    error.setText("Unable to establish connection.");
    error.setVisible(false);
    connectModel.addObserver(
        () -> {
          error.setText("Unable to establish connection.");
          error.setVisible(connectModel.isError());
          connect.setDisable(false);
        });
  }

  /**
   * Handles the Quit button.
   *
   * @param event the event that invoked the method
   */
  public void onQuitButton(ActionEvent event) {
    controller.onQuit();
  }

  /**
   * Handles the Connect button.
   *
   * @param event the event
   * @throws InterruptedException thrown when interrupted
   */
  public void onConnectButton(ActionEvent event) throws InterruptedException {
    if (port.getText().isEmpty() || ip.getText().isEmpty()) {
      return;
    }
    controller.onConnect(ip.getText(), Integer.parseInt(port.getText()));
    connect.setDisable(true);
    Thread.sleep(150);
    if (!connectModel.isError()) {
      error.setVisible(true);
      error.setText("Game in progress. Please wait...");
    }
  }
}
