package view;

import controller.GameOverController;
import data.PlayerScore;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import store.RankingStorage;
import store.SettingsStorage;
import store.UserStorage;

/** Implements the View of the Game Over window. */
public class GameOverView implements Initializable {

  @FXML Text wpm;
  @FXML Text placement;

  GameOverController controller;
  int place;

  @FXML javafx.scene.Parent parent;

  /** Constructors a GameOverView. */
  public GameOverView() {}

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    controller = new GameOverController();

    if (SettingsStorage.getInstance().isDarkmode()) {
      parent.getStylesheets().add("DarkMode.css");
    }

    RankingStorage.getInstance()
        .addObserver(
            () -> {
              String name = UserStorage.getInstance().getName();
              System.out.println(RankingStorage.getInstance().getOverallRanking());
              for (PlayerScore score : RankingStorage.getInstance().getOverallRanking()) {
                if (score.toString().contains(name))
                  place = RankingStorage.getInstance().getOverallRanking().indexOf(score) + 1;
              }
              String rank =
                  switch (place) {
                    case 1 -> name + ", you finished in 1st place!";
                    case 2 -> name + ", you finished in 2nd place!";
                    case 3 -> name + ", you finished in 3rd place!";
                    default -> name + ", you finished the race!";
                  };
              placement.setText(rank);
              wpm.setText(
                  "Your speed was: " + RankingStorage.getInstance().getWPMfor(name) + " wpm");
            });
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
   * Handles the Menu button.
   *
   * @param actionEvent the event that invoked the method
   */
  public void onMenuButton(ActionEvent actionEvent) {
    controller.onMenu();
  }
}
