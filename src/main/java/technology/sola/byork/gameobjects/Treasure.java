package technology.sola.byork.gameobjects;

import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;

public class Treasure implements GameObject {
  private static int treasureCount = 0;
  private final String id;
  private final int score;
  private boolean pickedUp = false;

  public Treasure(int score) {
    this.score = score;

    treasureCount++;

    id = "Treasure" + treasureCount;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return "Treasure";
  }

  @Override
  public String inspect() {
    return "Treasure!";
  }

  @Override
  public void use() {
    // Nothing to do here
  }

  @Override
  public void update() {
    // Nothing to do here
  }

  @Override
  public void pickup(Player player) {
    if (!pickedUp) {
      player.addToScore(score);
      pickedUp = true;
      UserInterface.getInstance().displayMessage("You found treasure!");
    } else {
      UserInterface.getInstance().displayMessage("It is empty.");
    }
  }

  @Override
  public void drop(Player player) {
    player.removeFromInventory(this);
  }
}

