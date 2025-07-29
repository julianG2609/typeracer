import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import serverui.model.MainModel;
import serverui.util.SceneLoader;
import serverui.util.Scenes;

/** Main Class. */
public class Main extends Application {

  /** Constructs a Main. */
  public Main() {}

  /**
   * Entrypoint.
   *
   * @param args Arguments
   */
  public static void main(String[] args) {
    if (args.length != 0 && args[0].equals("headless")) {
      MainModel.getInstance().setHeadless(true);
      System.out.println("Headless mode enabled");
    }
    launch(args);
  }

  /**
   * Javafx entrypoint.
   *
   * @param stage the primary stage
   */
  @Override
  public void start(Stage stage) {
    stage.setTitle("Type Racer Server");
    stage.getIcons().add(new Image("images/icon.png"));
    SceneLoader.init(stage);
    SceneLoader.getInstance().loadScene(Scenes.MainWindow);
    stage.show();
  }
}
