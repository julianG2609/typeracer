package util;

import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Helper Class to simplify loading of scenes. */
public class SceneLoader {
  private static SceneLoader instance;

  private final Stage primaryStage;

  /**
   * Sets a new SceneLoader with a stage.
   *
   * @param primaryStage the primary stage to draw the scenes on
   */
  public static void init(Stage primaryStage) {
    instance = new SceneLoader(primaryStage);
  }

  /**
   * Gets the instance of the Singleton.
   *
   * @return the instance of SceneLoader
   * @throws IllegalStateException if the SceneLoader has not been initialized
   */
  public static synchronized SceneLoader getInstance() {
    if (instance == null) {
      throw new IllegalStateException("SceneLoader has not been initialized.");
    }
    return instance;
  }

  private SceneLoader(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Loads the specified Scene.
   *
   * @param scene the scene to load
   */
  public void loadScene(Scenes scene) {
    Parent root = getScene(scene);
    Platform.runLater(
        () -> {
          primaryStage.setScene(new Scene(root));
          adjustMinWindowSize();
          primaryStage.centerOnScreen();
        });
  }

  /**
   * Loads the specified Scene while setting data to its root.
   *
   * @param scene the Scene to load
   * @param data the Data to set
   */
  public void loadScene(Scenes scene, Object data) {
    Parent root = getScene(scene);
    Platform.runLater(
        () -> {
          Scene sceneObj = new Scene(root);
          root.getProperties().put("status", data);
          primaryStage.setScene(sceneObj);
          adjustMinWindowSize();
          primaryStage.centerOnScreen();
        });
  }

  private Parent getScene(Scenes scene) {
    URL sceneUrl = getClass().getClassLoader().getResource(scene.file);
    if (sceneUrl == null) {
      throw new RuntimeException("Unable to find scene " + scene);
    }
    try {
      return FXMLLoader.load(sceneUrl);
    } catch (IOException e) {
      throw new RuntimeException("IOException while loading scene " + scene, e);
    }
  }

  private void adjustMinWindowSize() {
    primaryStage.setMinWidth(0);
    primaryStage.setMinHeight(0);
    primaryStage.getScene().getWindow().sizeToScene();
    Platform.runLater(
        () -> {
          primaryStage.setMinWidth(primaryStage.getWidth());
          primaryStage.setMinHeight(primaryStage.getHeight());
        });
  }
}
