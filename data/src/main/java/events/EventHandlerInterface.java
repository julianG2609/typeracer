package events;

/** The interface Event handler interface. */
public interface EventHandlerInterface {
  /**
   * Handle player join event.
   *
   * @param event the handlePlayerJoinEvent
   */
  void handlePlayerJoinEvent(PlayerJoinEvent event);

  /**
   * Handle start game event.
   *
   * @param event the StartGameEvent
   */
  void handleStartGameEvent(StartGameEvent event);

  /**
   * Handle player leave event.
   *
   * @param event the PlayerLeaveEvent
   */
  void handlePlayerLeaveEvent(PlayerLeaveEvent event);

  /**
   * Handle username taken event.
   *
   * @param event the UsernameTakenEvent
   */
  void handleUsernameTakenEvent(UsernameTakenEvent event);

  /**
   * Handle set car colour event.
   *
   * @param event the SetCarColourEvent
   */
  void handleSetCarColourEvent(SetCarColourEvent event);

  /**
   * Handle car colour taken event.
   *
   * @param event the CarColourTakenEvent
   */
  void handleCarColourTakenEvent(CarColourTakenEvent event);

  /**
   * Handle set username event.
   *
   * @param event the SetUsernameEvent
   */
  void handleSetUsernameEvent(SetUsernameEvent event);

  /**
   * Handle stop game event.
   *
   * @param event the StopGameEvent
   */
  void handleStopGameEvent(StopGameEvent event);

  /**
   * Handle player finish event.
   *
   * @param event the PlayerFinishEvent
   */
  void handlePlayerFinishEvent(PlayerFinishEvent event);

  /**
   * Handle create game event.
   *
   * @param event the CreateGameEvent
   */
  void handleCreateGameEvent(CreateGameEvent event);

  /**
   * Handles a player score event.
   *
   * @param event the PlayerScoreEvent
   */
  void handlePlayerScoreEvent(PlayerScoreEvent event);

  /**
   * Handle text event.
   *
   * @param event the TextEvent
   */
  void handleTextEvent(TextEvent event);

  /**
   * Handle a remote player object.
   *
   * @param player the remote player object
   */
  void handleRemotePlayer(Object player);
}
