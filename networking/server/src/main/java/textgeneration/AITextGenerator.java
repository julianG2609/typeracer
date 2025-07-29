package textgeneration;

import com.github.tjake.jlama.model.AbstractModel;
import com.github.tjake.jlama.model.ModelSupport;
import com.github.tjake.jlama.model.functions.Generator;
import com.github.tjake.jlama.safetensors.DType;
import com.github.tjake.jlama.safetensors.SafeTensorSupport;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Implements the text generation via an AI model */
public class AITextGenerator {

  private static final AITextGenerator INSTANCE = new AITextGenerator();

  /**
   * Singleton getter.
   *
   * @return the singleton instance of AIGenerator
   */
  @SuppressFBWarnings("MS_EXPOSE_REP")
  public static AITextGenerator getInstance() {
    return INSTANCE;
  }

  private static final String model = "tjake/Llama-3-8B-Instruct-jlama-Q4";
  private static final String workingDirectory = "./models";

  private String systemprompt =
      "You are the backend to a typing practice platform. You generate short texts for a given"
          + " topic, that are suitable for typing practise. The text's should not exceed 3"
          + " sentences.";

  private String userprompt = "Generate me a text on the topic of //topic//.";

  private List<String> topics =
      List.of("adventures", "technology", "mythology", "environment", "philosophy", "music");

  private final BlockingQueue<String> texts = new ArrayBlockingQueue<>(1);

  private final GeneratorThread generatorThread = new GeneratorThread();

  private final Random rand = new Random();

  private final AbstractModel m;

  private AITextGenerator() {

    File localModelPath = null;
    try {
      localModelPath = SafeTensorSupport.maybeDownloadModel(workingDirectory, model);
    } catch (IOException e) {
      // throw new RuntimeException(e);
    }

    // Loads the quantized model and specified use of quantized memory
    m = ModelSupport.loadModel(localModelPath, DType.F32, DType.I8);

    generatorThread.setDaemon(true);
    generatorThread.start();
  }

  /**
   * Get a generated text from the AI. Blocks until a text is available.
   *
   * @return the generated text
   * @throws InterruptedException if the waiting is interrupted
   */
  public String getText() throws InterruptedException {
    synchronized (generatorThread) {
      String tmp = texts.take();
      generatorThread.notify();
      return tmp;
    }
  }

  //  public void setPromptsAndRegenerate(String systemprompt, String userprompt) {
  //    this.systemprompt = systemprompt;
  //    this.userprompt = userprompt;
  //    texts.clear();
  //  }

  /** Stop the AI generator. */
  public void stop() {
    generatorThread.interrupt();
  }

  private class GeneratorThread extends Thread {
    @Override
    public void run() {
      interrupted();
      try {
        while (true) {
          synchronized (generatorThread) {
            if (texts.remainingCapacity() == 0) {
              generatorThread.wait();
            }
          }
          String text = generateText();
          texts.put(text);
        }
      } catch (InterruptedException ignored) {
      }
    }

    private String generateText() {
      String prompt =
          m.promptSupport()
              .get()
              .newBuilder()
              .addSystemMessage(systemprompt)
              .addUserMessage(
                  userprompt.replace("//topic//", topics.get(rand.nextInt(topics.size()))))
              .build();
      Generator.Response response =
          m.generate(UUID.randomUUID(), prompt, 0.5f, 512, false, (k, f) -> {});
      String[] lines = response.text.split("\n\r?\n\r?");
      System.out.println(Arrays.toString(lines));
      String text = lines[1];
      if (text.charAt(0) == '"') {
        text = text.substring(1, text.length() - 1);
      }
      return text;
    }
  }
}
