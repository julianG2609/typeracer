import static org.junit.jupiter.api.Assertions.*;

import gamelogic.Game;
import gamelogic.GameState;
import gamelogic.LocalPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.GameStorage;

public class GameTest {
  private Game game;
  private LocalPlayer player;

  /** Set up the test environment. */
  @BeforeEach
  public void setUp() {
    game = new Game();
    game.setText("This is a test text.");
    player = new LocalPlayer(0, "Test Player", "Blue");
    GameStorage.getInstance().setGame(game);
  }

  /** Test the initial state of the game. */
  @Test
  public void testInitialState() {
    assertEquals(GameState.WAITING, game.getState());
  }

  /** Test the start of the game. */
  @Test
  public void testStartGame() {
    game.startGame();
    assertEquals(GameState.IN_PROGRESS, game.getState());
  }

  /** Test the game state change to FINISHED */
  @Test
  public void testGameStateChange() {
    game.setState(GameState.FINISHED);
    assertEquals(GameState.FINISHED, game.getState());
  }

  /** Test the game state change to QUIT */
  @Test
  public void testQuitGame() {
    game.quitGame();
    assertEquals(GameState.QUIT, game.getState());
  }

  /** Test adding a player to the game */
  @Test
  public void testAddPlayer() {
    game.addPlayer(player);
    assertTrue(game.getPlayers().contains(player));
  }

  /** Test removing a player from the game */
  @Test
  public void testRemovePlayer() {
    game.addPlayer(player);
    game.removePlayer(player);
    assertFalse(game.getPlayers().contains(player));
  }

  /** Test the onInput method with correct input (single character) */
  @Test
  public void testOnInputCorrect() {
    game.startGame();
    game.addPlayer(player);
    String firstLetter = String.valueOf(game.getText().charAt(0));
    player.onInput(firstLetter);
    assertEquals(1, player.getCurrentCharIndex());
  }

  /** Test the onInput method with incorrect input */
  @Test
  public void testOnInputIncorrect() {
    game.startGame();
    game.addPlayer(player);
    String input = "incorrect";
    for (char c : input.toCharArray()) {
      player.onInput(String.valueOf(c));
    }
    assertNotEquals(input.length(), player.getCurrentCharIndex());
  }

  /** Test the onInput method with empty input */
  @Test
  public void testOnInputEmpty() {
    game.startGame();
    game.addPlayer(player);
    player.onInput("");
    assertEquals(0, player.getCurrentCharIndex());
  }

  /** Test player progress tracking */
  @Test
  public void testPlayerProgress() {
    game.startGame();
    game.addPlayer(player);
    String firstLetter = String.valueOf(game.getText().charAt(0));
    player.onInput(firstLetter);
    assertEquals(1, player.getCurrentCharIndex());
  }

  /** Test multiple players in the game */
  @Test
  public void testMultipleLocalPlayers() {
    LocalPlayer player2 = new LocalPlayer(1, "Second Player", "Red");
    game.addPlayer(player);
    game.addPlayer(player2);
    assertEquals(2, game.getPlayers().size());
  }

  /** Test game text assignment */
  @Test
  public void testGameTextAssignment() {
    game.startGame();
    assertNotNull(game.getText());
    assertNotNull(game.getText());
    assertFalse(game.getText().isEmpty());
  }

  /** Test game state transitions */
  @Test
  public void testGameStateTransitions() {
    assertEquals(GameState.WAITING, game.getState());
    game.startGame();
    assertEquals(GameState.IN_PROGRESS, game.getState());
    game.setState(GameState.FINISHED);
    assertEquals(GameState.FINISHED, game.getState());
    game.quitGame();
    assertEquals(GameState.QUIT, game.getState());
  }

  /** Test player initialization */
  @Test
  public void testPlayerInitialization() {
    LocalPlayer newPlayer = new LocalPlayer(1, "New Player", "Green");
    assertEquals(1, newPlayer.getId());
    assertEquals("New Player", newPlayer.getName());
    assertEquals("Green", newPlayer.getColour());
    assertEquals(0, newPlayer.getCurrentCharIndex());
  }

  /** Test concurrent player inputs */
  @Test
  public void testConcurrentPlayerInputs() {
    game.startGame();
    LocalPlayer player2 = new LocalPlayer(1, "Second Player", "Red");
    game.addPlayer(player);
    game.addPlayer(player2);
    String firstLetter = String.valueOf(game.getText().charAt(0));
    player.onInput(firstLetter);
    player2.onInput(firstLetter);
    assertEquals(1, player.getCurrentCharIndex());
    assertEquals(1, player2.getCurrentCharIndex());
  }
}
