package technology.sola.byork.gameobjects;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;

public class Scroll implements GameObject {
  private static int scrollCount = 0;
  private final String id;
  private final String helpfulMessage;

  /**
   * Builds this Scroll object. It is initialized with a wise message.
   *
   * @param helpfulMessage The wise message
   */
  public Scroll(String helpfulMessage) {
    this.helpfulMessage = helpfulMessage;

    scrollCount++;
    id = "Scroll" + scrollCount;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return "Scroll";
  }

  @Override
  public String inspect() {
    return helpfulMessage;
  }

  @Override
  public void use() {
    throw new RuntimeException("Scrolls should not be able to be used!");
  }

  @Override
  public void update() {
    // Nothing to do here
  }

  @Override
  public void pickup(Player player) {
    UserInterface.getInstance().displayErrorMessage(
      "It's best to leave it for someone else.");
  }

  @Override
  public void drop(Player player) {
    // Nothing to do here
  }

}

