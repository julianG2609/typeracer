package serverui.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import server.Server;
import serverui.util.Observable;

/** The Main model for the server UI. */
public class MainModel extends Observable<MainModel> {

  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  private static final MainModel INSTANCE = new MainModel();

  private boolean useAI;

  /**
   * Gets instance.
   *
   * @return the instance
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static MainModel getInstance() {
    return INSTANCE;
  }

  private MainModel() {
    initLogging();
  }

  private int port = 0;

  private boolean running = false;

  private Server server;

  private boolean headless = false;

  /**
   * Sets if the server is headless.
   *
   * @param headless true if the server is headless, false otherwise
   */
  public void setHeadless(boolean headless) {
    this.headless = headless;
  }

  /**
   * Gets if the server is running.
   *
   * @return true if the server is running, false otherwise
   */
  public boolean isRunning() {
    return running;
  }

  /** Flips the running state of the server. */
  public void flipRunning() {
    running = !running;
    notifyObservers();
    executor.execute(
        () -> {
          if (running) {
            server = new Server(useAI);
            try {
              server.connect(port);
            } catch (IOException e) {
              flipRunning();
            }
          } else {
            server.disconnect();
            server = null;
          }
        });
  }

  /**
   * Sets the server port.
   *
   * @param port the port to serve on
   */
  public void setPort(int port) {
    this.port = port;
  }

  /**
   * Sets to use the AI on the next start.
   *
   * @param useAI if true uses AI
   */
  public void setUseAI(boolean useAI) {
    this.useAI = useAI;
  }

  /** Flips the running state of the server if it is not headless. */
  public void onQuit() {
    if (!headless) {
      if (running) flipRunning();
      executor.shutdownNow();
    }
  }

  private final Logger logger = Logger.getLogger(Server.class.getName());

  private void initLogging() {
    try {
      logger.setLevel(Level.ALL);
      logger.addHandler(listLogger);
    } catch (SecurityException ignored) {
    }
  }

  private final ListLogger listLogger = new ListLogger();

  private final List<LogHandler> logHandlers = new ArrayList<>();

  /**
   * Adds a log handler.
   *
   * @param logHandler the log handler to add
   */
  public void addLogHandler(LogHandler logHandler) {
    logHandlers.add(logHandler);
  }

  /**
   * Removes a log handler.
   *
   * @param logHandler the log handler to remove
   */
  public void removeLogHandler(LogHandler logHandler) {
    logHandlers.remove(logHandler);
  }

  /** Defines a handler for processing log records. */
  public interface LogHandler {

    /**
     * Processes a given log record.
     *
     * @param record the log record to process
     */
    void log(LogRecord record);
  }

  private class ListLogger extends Handler {

    @Override
    public void publish(LogRecord record) {
      logHandlers.forEach(
          (logHandler) -> {
            logHandler.log(record);
          });
    }

    @Override
    public void flush() {}

    @Override
    public void close() throws SecurityException {
      logHandlers.clear();
    }
  }
}
