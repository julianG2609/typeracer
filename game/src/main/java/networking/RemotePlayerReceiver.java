package networking;

import endpoints.synchronisation.Synchronizer;
import events.EventHandlerInterface;
import gamelogic.RemotePlayer;

/** The type Remote player receiver. */
public class RemotePlayerReceiver implements Synchronizer.SynchronizedCreator<RemotePlayer> {

  private EventHandlerInterface eventHandler;

  RemotePlayerReceiver(EventHandlerInterface eventHandler) {
    this.eventHandler = eventHandler;
  }

  @Override
  public RemotePlayer createObject() {
    return new RemotePlayer(0, "", "");
  }

  @Override
  public void handleObject(RemotePlayer obj) {
    System.out.println("Received remote player: " + obj);
    eventHandler.handleRemotePlayer(obj);
  }
}
