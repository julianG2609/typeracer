package server.util;

import java.util.Random;

/** Text Klasse. */
public class Text {
  private String content;
  private static final String[] groupA = {
    "The journey of a thousand miles begins with a single step.",
    "The stars in the night sky guide sailors on their voyages.",
    "Innovation distinguishes between a leader and a follower.",
    "In the midst of chaos, there is also opportunity.",
    "With great power comes great responsibility.",
    "The only limit to our realization of tomorrow is our doubts of today.",
    "Success is not final, failure is not fatal. It is the courage to continue that counts.",
    "The best way to predict the future is to invent it.",
    "You miss 100 percent of the shots you do not take.",
    "The only thing we have to fear is fear itself."
  };

  private static final String[] groupB = {
    "To infinity and beyond!",
    "Elementary, my dear Watson.",
    "Houston, we have a problem.",
    "May the Force be with you.",
    "The only thing we have to fear is fear itself.",
    "I think, therefore I am.",
    "All the world is a stage, and all the men and women merely players.",
    "To be or not to be, that is the question.",
    "In the end, we will remember not the words of our enemies, but the silence of our friends.",
    "I have a dream that one day this nation will rise up and live out the true meaning of its"
        + " creed."
  };

  private static final String[] groupC = {
    "Believe you can and you are halfway there.",
    "Do not watch the clock, do what it does. Keep going.",
    "Act as if what you do makes a difference. It does.",
    "Success is not how high you have climbed, but how you make a positive difference to the"
        + " world.",
    "Keep your face always toward the sunshine—and shadows will fall behind you.",
    "The only way to do great work is to love what you do.",
    "The purpose of our lives is to be happy.",
    "Life is what happens when you are busy making other plans.",
    "You only live once, but if you do it right, once is enough.",
    "In the end, it is not the years in your life that count. It is the life in your years."
  };

  private static final String[] groupD = {
    "Every great dream begins with a dreamer. Always remember, you have within you the strength,"
        + " the patience, and the passion to reach for the stars to change the world.",
    "The best and most beautiful things in the world cannot be seen or even touched - they must be"
        + " felt with the heart.",
    "It is our choices, that show what we truly are, far more than our abilities.",
    "Success is not the key to happiness. Happiness is the key to success. If you love what you are"
        + " doing, you will be successful.",
    "Keep your eyes on the stars, and your feet on the ground.",
    "Do not follow where the path may lead. Go instead where there is no path and leave a trail.",
    "Challenges are what make life interesting and overcoming them is what makes life meaningful.",
    "You have brains in your head. You have feet in your shoes. You can steer yourself any"
        + " direction you choose.",
    "The only way to achieve the impossible is to believe it is possible.",
    "Success usually comes to those who are too busy to be looking for it."
  };

  private static final Random random = new Random();

  /**
   * Konstruktor der Text Klasse.
   *
   * @param content Inhalt des Texts
   */
  public Text(String content) {
    this.content = content;
  }

  /**
   * Getter Methode für den Inhalt des Texts.
   *
   * @return Inhalt des Texts
   */
  public String getContent() {
    return content;
  }

  /**
   * Methode um einen zufälligen Text zu generieren.
   *
   * @return zufälliger Text
   */
  public static Text getRandomText() {
    String sentenceA = groupA[random.nextInt(groupA.length)];
    String sentenceB = groupB[random.nextInt(groupB.length)];
    String sentenceC = groupC[random.nextInt(groupC.length)];
    String sentenceD = groupD[random.nextInt(groupD.length)];
    String randomText = sentenceA + " " + sentenceB + " " + sentenceC + " " + sentenceD;
    return new Text(randomText);
  }
}
