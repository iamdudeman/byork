package technology.sola.byork.gameobjects;

import technology.sola.byork.Main;
import technology.sola.byork.Player;
import technology.sola.byork.UserInterface;

import java.util.Iterator;

public class Flashlight implements GameObject {
  private static int flashlightCount = 0;
  private final String id;
  private int batteryLife = 0;
  private boolean isOn = false;

  /**
   * Builds this Flashlight object. It is initialized to have a certain
   * battery life that decays as the player moves.
   *
   * @param batteryLife The starting battery life
   */
  public Flashlight(int batteryLife) {
    this.batteryLife = batteryLife;

    flashlightCount++;

    id = "Flashlight" + flashlightCount;
  }

  public void addPower(int battery) {
    batteryLife += battery;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return "Flashlight";
  }

  @Override
  public String inspect() {
    return "A flashlight with " + batteryLife + " battery left.";
  }

  @Override
  public void use() {
    if (!isOn) {
      batteryLife--;
    }

    if (!isOn && batteryLife > 0) {
      isOn = true;
      Main.getPlayer().setCanSeeInDark(true);
      UserInterface.getInstance().displayMessage("Your flashlight is on. " + batteryLife + " battery remaining.");
    } else if (isOn) {
      isOn = false;
      Main.getPlayer().setCanSeeInDark(false);
      UserInterface.getInstance().displayMessage("Your flashlight is off. " + batteryLife + " battery remaining.");
    }
  }

  @Override
  public void update() {
    if (isOn) {
      batteryLife--;
      if (batteryLife <= 0) {
        isOn = false;
        Main.getPlayer().setCanSeeInDark(false);
        UserInterface.getInstance().displayMessage("Your flashlight ran out of power.");
      } else {
        Main.getPlayer().setCanSeeInDark(true);
        UserInterface.getInstance().displayMessage("Your flashlight is on. " + batteryLife + " battery remaining.");
      }
    }
  }

  @Override
  public void pickup(Player player) {
    Flashlight heldFlashlight = null;
    Iterator<GameObject> iter = player.getInventoryIter();
    while (iter.hasNext()) {
      GameObject current = iter.next();
      if (current.getName().equals(getName())) {
        heldFlashlight = (Flashlight) current;
        break;
      }
    }

    if (heldFlashlight == null) {
      player.addToInventory(this);
    } else {
      UserInterface.getInstance().displayMessage(batteryLife + " battery added to flashlight.");
      heldFlashlight.addPower(batteryLife);
      batteryLife = 0;
    }
  }

  @Override
  public void drop(Player player) {
    player.removeFromInventory(this);
  }
}

