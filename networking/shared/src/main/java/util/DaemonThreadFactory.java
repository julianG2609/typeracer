package util;

import java.util.concurrent.ThreadFactory;

/** Factory to create Daemonthreads. */
public class DaemonThreadFactory implements ThreadFactory {

  /** Creates a new default DaemonThreadFactory. */
  public DaemonThreadFactory() {}

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r);
    t.setDaemon(true);
    return t;
  }
}
