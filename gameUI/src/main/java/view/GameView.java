package view;

import controller.GameController;
import gamelogic.Game;
import gamelogic.LocalPlayer;
import gamelogic.Player;
import java.net.URL;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import store.SettingsStorage;

/** Implements the View of the Game window. */
public class GameView implements Initializable {

  private static final int CAR_SCALING_FACTOR = 12;
  private static final int CAR_PADDING = 14;
  private static final double CAR_TRANSLATION_SCALING_FACTOR = 0.86;
  private static final int LANE_SEPARATOR_SCALING_FACTOR = 250;
  private static final int LANE_SEPARATOR_TRANSLATION_FACTOR = 100;
  private static final int CURB_SCALING_FACTOR = 100;
  private static final int CURB_OFFSET = 10;
  private static final int START_LINE_TRANSLATION_FACTOR = 10;
  private static final int LINE_LABEL_SCALING_FACTOR = 50;
  private static final double FINISH_LINE_TRANSLATION_FACTOR = 1.15;
  private static final int RACE_LIGHT_SCALING_FACTOR = 28;
  private static final int MEDAL_SCALING_FACTOR = 2 * CAR_SCALING_FACTOR;
  private static final int MEDAL_SPACING_FACTOR = CAR_SCALING_FACTOR / 3;
  private static final int MAX_FONT_SIZE = 80;
  private static final int MIN_FONT_SIZE = 10;
  private static final double REDUCTION_FACTOR = 0.95;
  private static final int PADDING_SIZE = 20;
  private static final String TEXT_COLOR_CORRECT = "#75ff8c";
  private static final String TEXT_COLOR_WRONG = "#ff7575";
  private static final String TEXT_COLOR_NEXT = "#fff157";
  private static final String TEXT_COLOR_CORRECT_DARK = "#2ba65b";
  private static final String TEXT_COLOR_WRONG_DARK = "#b33030";
  private static final String TEXT_COLOR_NEXT_DARK = "#ccb300";

  GameController controller;

  @FXML private VBox racetrack;
  @FXML private Pane curbTop, curbBottom;
  @FXML private VBox typingPane, innerTypingPane;
  @FXML private TextFlow typingText;
  @FXML private VBox lineStart, lineFinish;
  @FXML private ImageView imageStart, imageFinish;
  @FXML private Button quitButton;
  @FXML private VBox parent;
  @FXML private VBox raceLight;
  @FXML private Pane raceLightContainerRed, raceLightContainerGreen;
  @FXML private Text timer;
  @FXML private StackPane finishMessage;

  private List<HBox> carsWithMedals;
  private List<VBox> carsWithLabels;
  private List<ImageView> cars;
  private List<Pane> laneSeparators;
  private List<Pane> redTrafficLights;
  private List<Pane> greenTrafficLights;
  private List<Pane> medals;

  private final Stack<String> medalsImgPaths = new Stack<>();
  private final Set<Integer> finishedCars = new HashSet<>();

  private String textColorCorrect = TEXT_COLOR_CORRECT;
  private String textColorWrong = TEXT_COLOR_WRONG;
  private String textColorNext = TEXT_COLOR_NEXT;

  boolean textInitializationRan = false;
  private final List<StackPane> typingTextCharacters = new ArrayList<>();

  /** Constructors a GameView. */
  public GameView() {
    medalsImgPaths.push("images/medals/bronze.png");
    medalsImgPaths.push("images/medals/silver.png");
    medalsImgPaths.push("images/medals/gold.png");
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    controller = new GameController();

    if (SettingsStorage.getInstance().isDarkmode()) {
      parent.getStylesheets().add("DarkMode.css");
      textColorCorrect = TEXT_COLOR_CORRECT_DARK;
      textColorWrong = TEXT_COLOR_WRONG_DARK;
      textColorNext = TEXT_COLOR_NEXT_DARK;
    }

    carsWithMedals = getCarsWithMedals();
    carsWithLabels = getCarsWithLabels();
    laneSeparators = getLaneSeparators();
    cars = getCars();
    redTrafficLights = getTrafficLights(raceLightContainerRed);
    greenTrafficLights = getTrafficLights(raceLightContainerGreen);
    medals = getMedals();

    bindUI();
    initInput();
    controller.onStart();

    finishMessage.setVisible(false);

    initializeUiElements();
  }

  private void initializeUiElements() {
    updateLabels(0, "playerName", controller.getGame().getPlayers().getFirst().getName());
    controller.game.addObserver(
        () -> {
          if (controller.game.getText() != null) {
            Platform.runLater(
                () -> {
                  updateWindowSize();
                  initializeText();
                  initializeDynamicScaling();
                  initializeCars();
                  initializeTrafficLight();
                });
          }
        });
  }

  private void updateLabels(int index, String playerName, String controller) {
    carsWithLabels.get(index).getChildren().stream()
        .filter((label) -> label instanceof Label && label.getStyleClass().contains(playerName))
        .findFirst()
        .ifPresent((label) -> Platform.runLater(() -> ((Label) label).setText(controller)));
  }

  private void updateWindowSize() {
    parent
        .sceneProperty()
        .addListener(
            (ignored, oldScene, newScene) -> {
              if (newScene != null) {
                newScene
                    .windowProperty()
                    .addListener(
                        (ignored2, oldWindow, newWindow) -> {
                          if (newWindow != null) {
                            Platform.runLater(
                                () -> {
                                  Stage primaryStage = (Stage) newWindow;
                                  primaryStage.setMinWidth(0);
                                  primaryStage.setMinHeight(0);
                                  primaryStage.getScene().getWindow().sizeToScene();
                                  primaryStage.setMinWidth(primaryStage.getWidth());
                                  primaryStage.setMinHeight(primaryStage.getHeight());
                                });
                          }
                        });
              }
            });
    //    Platform.runLater(
    //        () -> {
    //          Stage primaryStage;
    //          try {
    //            primaryStage = (Stage) parent.getScene().getWindow();
    //          } catch (Exception e) {
    //            return;
    //          }
    //
    //
    //        });
  }

  private void initializeText() {
    if (textInitializationRan) return;
    textInitializationRan = true;
    String text = controller.getGame().getText();
    String[] words = text.split(" ");
    for (String word : words) {
      TextFlow wordPane = new TextFlow();
      for (char c : word.toCharArray()) {
        Text cText = new Text(String.valueOf(c));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(cText);
        wordPane.getChildren().add(stackPane);
        typingTextCharacters.add(stackPane);
      }
      Text cText = new Text(" ");
      StackPane stackPane = new StackPane();
      stackPane.getChildren().add(cText);
      wordPane.getChildren().add(stackPane);
      typingTextCharacters.add(stackPane);
      typingText.getChildren().add(wordPane);
    }
    updateMarkers((LocalPlayer) controller.game.players.getFirst());
  }

  private void initializeDynamicScaling() {

    bindCarSizes(parent);
    bindLaneSeparators(parent);
    bindRacetrackElements(parent);
    bindTypingPane(parent);
    bindTrafficLights(parent);
    bindFinishMessage(parent);
    bindMedals(parent);

    setupFontSizeAdjustment();
  }

  private void bindMedals(VBox stage) {
    medals.forEach(
        medal ->
            medal.prefHeightProperty().bind(stage.widthProperty().divide(MEDAL_SCALING_FACTOR)));
    carsWithMedals.forEach(
        carWithMedal ->
            carWithMedal
                .maxWidthProperty()
                .bind(stage.widthProperty().divide(CAR_SCALING_FACTOR).add(CAR_PADDING)));
  }

  private void bindTrafficLights(VBox stage) {
    raceLight
        .translateXProperty()
        .bind(
            racetrack
                .widthProperty()
                .divide(2)
                .subtract(raceLightContainerRed.widthProperty().divide(2)));
    raceLight
        .translateYProperty()
        .bind(
            racetrack.heightProperty().divide(2).subtract(raceLightContainerRed.heightProperty()));

    bindLightsSize(redTrafficLights, stage);
    bindLightsSize(greenTrafficLights, stage);
  }

  private void bindLightsSize(List<Pane> redTrafficLights, VBox stage) {
    redTrafficLights.forEach(
        light -> {
          light.prefWidthProperty().bind(stage.widthProperty().divide(RACE_LIGHT_SCALING_FACTOR));
          light.prefHeightProperty().bind(stage.widthProperty().divide(RACE_LIGHT_SCALING_FACTOR));
        });
  }

  private void bindFinishMessage(VBox stage) {
    finishMessage
        .translateXProperty()
        .bind(
            racetrack.widthProperty().divide(2).subtract(finishMessage.widthProperty().divide(2)));
    finishMessage
        .translateYProperty()
        .bind(
            racetrack
                .heightProperty()
                .divide(2)
                .subtract(finishMessage.heightProperty().divide(2).add(5)));
  }

  private void bindCarSizes(VBox stage) {
    cars.forEach(
        car -> car.fitWidthProperty().bind(stage.widthProperty().divide(CAR_SCALING_FACTOR)));
  }

  private void bindLaneSeparators(VBox stage) {
    laneSeparators.forEach(
        separator -> {
          separator
              .prefHeightProperty()
              .bind(stage.widthProperty().divide(LANE_SEPARATOR_SCALING_FACTOR));
          separator
              .translateXProperty()
              .bind(stage.widthProperty().divide(LANE_SEPARATOR_TRANSLATION_FACTOR));
        });
  }

  private void bindRacetrackElements(VBox stage) {
    racetrack.minWidthProperty().bind(stage.widthProperty());

    bindStartLine(stage);
    bindFinishLine(stage);

    curbTop
        .prefHeightProperty()
        .bind(stage.widthProperty().divide(CURB_SCALING_FACTOR).add(CURB_OFFSET));
    curbBottom
        .prefHeightProperty()
        .bind(stage.widthProperty().divide(CURB_SCALING_FACTOR).add(CURB_OFFSET));
  }

  private void bindStartLine(VBox stage) {
    lineStart
        .translateXProperty()
        .bind(stage.widthProperty().divide(START_LINE_TRANSLATION_FACTOR));
    lineStart.minHeightProperty().bind(racetrack.heightProperty());
    imageStart.fitWidthProperty().bind(stage.widthProperty().divide(LINE_LABEL_SCALING_FACTOR));
  }

  private void bindFinishLine(VBox stage) {
    lineFinish
        .translateXProperty()
        .bind(racetrack.widthProperty().divide(FINISH_LINE_TRANSLATION_FACTOR));
    lineFinish.minHeightProperty().bind(racetrack.heightProperty());
    imageFinish.fitWidthProperty().bind(stage.widthProperty().divide(LINE_LABEL_SCALING_FACTOR));
  }

  private void bindTypingPane(VBox stage) {
    typingText.maxWidthProperty().bind(stage.widthProperty().subtract(2 * PADDING_SIZE));
  }

  private void initializeCars() {
    for (int i = 0; i < controller.game.players.size(); i++) {
      String colour = controller.game.players.get(i).getColour();
      setCarColour(colour, i);
    }
    for (int i = controller.game.players.size(); i < carsWithLabels.size(); i++) {
      carsWithMedals.get(i).setVisible(false);
      carsWithMedals.get(i).setManaged(false);
      laneSeparators.get(i - 1).setVisible(false);
      laneSeparators.get(i - 1).setManaged(false);
    }
  }

  private void setCarColour(String colour, int i) {
    if (colour.trim().isEmpty()) {
      return;
    }
    String imgPath = cars.get(i).getImage().getUrl();
    if (imgPath.contains("undefined.png")) {
      imgPath = imgPath.replace("undefined.png", colour + ".png");
      cars.get(i).setImage(new javafx.scene.image.Image(imgPath));
    }
  }

  private void setupFontSizeAdjustment() {
    typingPane.widthProperty().addListener((observable, oldValue, newValue) -> adjustFontSize());
    typingPane.heightProperty().addListener((observable, oldValue, newValue) -> adjustFontSize());
  }

  private void adjustFontSize() {
    double fontSize = MAX_FONT_SIZE;
    double ratio;

    do {
      typingText.setStyle(String.format("-fx-font-size: %.1fpx;", fontSize));
      typingText.applyCss();
      innerTypingPane.layout();
      typingPane.layout();

      double availableHeight = typingPane.getHeight();
      ratio = innerTypingPane.getHeight() / availableHeight;

      fontSize = Math.min(fontSize *= REDUCTION_FACTOR, fontSize - 1);
    } while (ratio > 1 && fontSize > MIN_FONT_SIZE);
  }

  private List<ImageView> getCars() {
    return carsWithLabels.stream()
        .flatMap(carWithLabel -> carWithLabel.getChildren().stream())
        .filter(node -> node instanceof ImageView)
        .map(node -> (ImageView) node)
        .toList();
  }

  private List<Pane> getLaneSeparators() {
    return racetrack.getChildren().stream()
        .filter((node) -> node instanceof Pane && !(node instanceof HBox))
        .map((node) -> (Pane) node)
        .toList();
  }

  private List<HBox> getCarsWithMedals() {
    return racetrack.getChildren().stream()
        .filter((node) -> node instanceof HBox)
        .map((node) -> (HBox) node)
        .toList();
  }

  private List<VBox> getCarsWithLabels() {
    return carsWithMedals.stream()
        .flatMap(carWithLabel -> carWithLabel.getChildren().stream())
        .filter(node -> node instanceof VBox && node.getStyleClass().contains("carWithLabels"))
        .map(node -> (VBox) node)
        .toList();
  }

  private List<Pane> getTrafficLights(Pane container) {
    return container.getChildren().stream()
        .filter((node) -> node instanceof Pane)
        .map((node) -> (Pane) node)
        .toList();
  }

  private List<Pane> getMedals() {
    return carsWithMedals.stream()
        .flatMap(carsWithMedals -> carsWithMedals.getChildren().stream())
        .filter(node -> node instanceof VBox)
        .flatMap(vbox -> ((VBox) vbox).getChildren().stream())
        .filter(node -> node instanceof Pane && node.getStyleClass().contains("medal"))
        .map(node -> (Pane) node)
        .toList();
  }

  private void initInput() {
    quitButton.setFocusTraversable(false);
    racetrack.setFocusTraversable(true);
    parent.setOnKeyPressed(this::handleKeyPressed);
    typingPane.getChildren().getFirst().setOnKeyPressed(this::handleKeyPressed);
  }

  private void handleKeyPressed(KeyEvent event) {
    if (!Objects.equals(event.getText(), "")) {
      String character = event.getText();
      if (event.isShiftDown()) {
        character = character.toUpperCase();
      }
      controller.onType(character);
    }
    event.consume();
  }

  private final List<Integer> idList = new ArrayList<>();

  private void bindUI() {
    controller
        .getGame()
        .addObserver(
            () -> {
              Game game = controller.getGame();
              if (Objects.equals(game.getState(), "FINISHED")) {
                controller.onGameOver();
              }
              List<Player> allPlayers = controller.getGame().getPlayers();
              for (int i = 0; i < allPlayers.size(); i++) {
                Player player = allPlayers.get(i);
                if (idList.contains(player.getId())) {
                  continue;
                }
                int finalI = i;
                player.addObserver(
                    () -> {
                      System.out.print("observe");
                      moveCar(finalI, player.getProgress());
                      updateWPM(finalI, player.getWPM());
                      if (player.getClass().getSimpleName().equals("LocalPlayer")) {
                        updateMarkers((LocalPlayer) player);
                        if (player.getProgress() == 1) {
                          raceLight.setVisible(false);
                          finishMessage.setVisible(true);
                        }
                      } else {
                        updateLabels(
                            finalI,
                            "playerName",
                            controller.getGame().getPlayers().get(finalI).getName());
                      }
                      String colour = player.getColour();
                      setCarColour(colour, finalI);
                    });
                idList.add(player.getId());
              }
            });
  }

  private void initializeTrafficLight() {
    raceLightContainerRed.setVisible(true);
    raceLightContainerGreen.setVisible(true);

    Timeline timeline = new Timeline();

    for (int i = 0; i < redTrafficLights.size(); i++) {
      final int index = i;
      timeline
          .getKeyFrames()
          .add(
              new KeyFrame(
                  Duration.seconds(i + 1),
                  e -> redTrafficLights.get(index).setStyle("-fx-background-color: red")));
    }

    timeline
        .getKeyFrames()
        .addAll(
            new KeyFrame(
                Duration.seconds(redTrafficLights.size() + 1),
                e -> {
                  redTrafficLights.forEach(light -> light.setStyle("-fx-background-color: black"));
                  raceLightContainerGreen.setStyle("-fx-background-color: #37ff00");
                  controller.setProcessInput(true);
                }),
            new KeyFrame(
                Duration.seconds(redTrafficLights.size() + 2),
                e -> {
                  raceLightContainerRed.setVisible(false);
                  raceLightContainerGreen.setVisible(false);
                }));
    timeline.setCycleCount(1);
    controller
        .getGame()
        .addObserver(
            () -> {
              if (Objects.equals(controller.game.getState(), "IN_PROGRESS")) timeline.play();
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

  private void updateMarkers(LocalPlayer player) {
    int index = player.getCurrentCharIndex();
    Platform.runLater(
        () -> {
          if (index - 1 >= 0) {
            typingTextCharacters
                .get(index - 1)
                .setBackground(Background.fill(Paint.valueOf(textColorCorrect)));
          }
          String color = textColorWrong;
          if (player.isLastInput()) color = textColorNext;
          if (index < typingTextCharacters.size())
            typingTextCharacters.get(index).setBackground(Background.fill(Paint.valueOf(color)));
        });
  }

  /**
   * Moves the car.
   *
   * @param id the id of the car to move
   */
  private void moveCar(int id, double progress) {
    carsWithMedals
        .get(id)
        .translateXProperty()
        .bind(
            racetrack
                .widthProperty()
                .subtract(carsWithLabels.get(id).widthProperty())
                .subtract(CAR_PADDING)
                .multiply(CAR_TRANSLATION_SCALING_FACTOR)
                .multiply(progress));
    if (progress == 1 && !finishedCars.contains(id)) {
      finishedCars.add(id);
      System.out.println("Car " + id + " finished");
      showMedal(id);
      startTimer();
    }
  }

  private void showMedal(int id) {
    medals.get(id).prefWidthProperty().bind(racetrack.widthProperty().divide(MEDAL_SCALING_FACTOR));
    carsWithMedals
        .get(id)
        .spacingProperty()
        .bind(racetrack.widthProperty().divide(MEDAL_SCALING_FACTOR));
    carsWithMedals
        .get(id)
        .maxWidthProperty()
        .bind(racetrack.widthProperty().divide(MEDAL_SPACING_FACTOR));
    if (!medalsImgPaths.isEmpty()) {
      medals.get(id).setStyle("-fx-background-image: url('" + medalsImgPaths.pop() + "');");
    }
  }

  private void startTimer() {
    timer.setVisible(true);
    Timeline timeline = new Timeline();
    timeline.setCycleCount(30);
    timeline
        .getKeyFrames()
        .add(
            new KeyFrame(
                Duration.seconds(1),
                e -> {
                  int time =
                      Integer.parseInt(timer.getText().substring(timer.getText().length() - 2));
                  if (time == 0) {
                    controller.onGameOver();
                    timer.setVisible(false);
                    timeline.stop();
                  }
                  timer.setText("Time left 00:" + String.format("%02d", time - 1));
                }));
    timeline.play();
  }

  private void updateWPM(int id, int wpm) {
    updateLabels(id, "speedLabel", "speed: " + wpm + " wpm");
  }
}
