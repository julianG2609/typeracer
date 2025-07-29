package controller;

import model.ConnectModel;
import store.ConnectionStore;
import util.SceneLoader;
import util.Scenes;

/** Constructs a ConnectController. */
public class ConnectController {

  /** The connect Model. */
  public ConnectModel model;

  /**
   * Constructs a ConnectController.
   *
   * @param model the connect model
   */
  public ConnectController(ConnectModel model) {
    this.model = model;
  }

  /** Handles the Quit button. */
  public void onQuit() {
    SceneLoader.getInstance().loadScene(Scenes.QuitWindow, Scenes.ConnectWindow);
  }

  /**
   * Handles the Connect button.
   *
   * @param ip the ip address of the server
   * @param port the port number of the server
   */
  public void onConnect(String ip, int port) {
    if (ip.isEmpty() || port < 1 || port > 65535) {
      return;
    }
    // TODO Connection
    System.out.println("Connect Event: " + ip + ":" + port);
    model.showError(false);
    ConnectionStore.getInstance()
        .getClient()
        .connect(
            ip,
            port,
            () -> {
              model.showError(true);
            },
            () -> {
              SceneLoader.getInstance().loadScene(Scenes.StartGameWindow);
            });
  }
}
