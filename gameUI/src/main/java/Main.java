import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.SceneLoader;
import util.Scenes;

/** Main Class. */
public class Main extends Application {
  /** Default constructor for javafx. */
  public Main() {}

  /**
   * Javafx entrypoint.
   *
   * @param stage the primary stage
   */
  @Override
  public void start(Stage stage) {
    stage.setTitle("Type Racer");
    stage.getIcons().add(new Image("images/misc_assets/icon.png"));
    SceneLoader.init(stage);
    SceneLoader.getInstance().loadScene(Scenes.ConnectWindow);
    stage.show();
  }

  /**
   * Application entrypoint.
   *
   * @param args Program arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
