package gamelogic;

import store.GameStorage;

/** The LocalPlayer class represents a player who interacts locally with the game. */
public class LocalPlayer extends Player {

  private final transient WPMTracker tracker;

  /**
   * Instantiates a new LocalPlayer.
   *
   * @param id the player ID.
   * @param name the player name.
   * @param colour the car colour of the player.
   */
  public LocalPlayer(int id, String name, String colour) {
    super(id, name, colour);
    tracker = new WPMTracker();
  }

  private transient boolean lastInput = true;

  private transient int currentCharIndex = 0;

  private transient int wordsTyped;

  /**
   * Processes user inputs.
   *
   * @param input the input
   */
  public void onInput(String input) {
    Game game = GameStorage.getInstance().getGame();
    if (input.length() != 1) {
      return;
    }
    if (currentCharIndex >= game.getText().length()) return;
    char currentChar = game.getText().charAt(currentCharIndex);
    if (currentChar == input.charAt(0)) {
      currentCharIndex++;
      if (input.charAt(0) == ' ') {
        wordsTyped++;
        wpm = tracker.calculateWPM(wordsTyped);
      }
      calculateProgress();
      if (currentCharIndex == game.getText().length()) {
        game.setState("FINISHED");
      }
      lastInput = true;
    } else {
      lastInput = false;
    }
    notifyObservers();
  }

  /** Calculates the player's progress in the game. */
  private void calculateProgress() {
    progress = ((float) currentCharIndex / GameStorage.getInstance().getGame().getText().length());
  }

  /**
   * Checks whether it is the last input.
   *
   * @return the boolean
   */
  public boolean isLastInput() {
    return lastInput;
  }

  /**
   * Gets current char index.
   *
   * @return the current char index
   */
  public int getCurrentCharIndex() {
    return currentCharIndex;
  }

  @Override
  public String toString() {
    return "[Local Player: " + super.toString() + "]";
  }
}
